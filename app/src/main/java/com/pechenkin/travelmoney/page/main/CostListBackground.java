package com.pechenkin.travelmoney.page.main;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.LongSparseArray;
import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.calculation.Calculation;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.GroupCost;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.list.AdapterCostList;

import java.util.ArrayList;
import java.util.List;

public class CostListBackground extends AsyncTask<Void, Void, Void> {

    private TripBaseTableRow trip;
    @SuppressLint("StaticFieldLeak")
    private ListView listViewCosts;
    private AdapterCostList adapter = null;
    private ProgressDialog procDialog;
    private Cost[] finalList = {};

    private PostRunner postRunner;

    public CostListBackground(ListView listViewCosts, TripBaseTableRow trip, PostRunner postRunner){
        this.trip = trip;
        this.listViewCosts = listViewCosts;
        this.postRunner = postRunner;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        procDialog = ProgressDialog.show(MainActivity.INSTANCE,
                "",
                MainActivity.INSTANCE.getString(R.string.wait), true);
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (this.trip != null) {

            //TimeMeter allTimer = new TimeMeter("Общее");
            CostQueryResult costList = t_costs.getAllByTripId(this.trip.id);

            Cost[] calculationList = {};
            if (costList.hasRows()) {
                calculationList = Calculation.call(costList.getAllRows());

                // Группируем, если есть группировка по цветам
                if (calculationList.length > 0 && t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)) {
                            /*
                               берем итоговый список, вместо id участников ставим id их цветов и закидываем еще раз на пересчет
                            */

                    //TimeMeter groupTimer = new TimeMeter("Группировка");

                    Cost[] calcListCosts = new Cost[calculationList.length];
                    LongSparseArray<Long> membersByColor = new LongSparseArray<>();
                    for (int i = 0; i < calculationList.length; i++) {

                        Cost calcCost = calculationList[i];
                        MemberBaseTableRow member = t_members.getMemberById(calcCost.member());
                        MemberBaseTableRow to_member = t_members.getMemberById(calcCost.to_member());

                        //В приоритете запомнить учатсника, кому должны
                        membersByColor.put(to_member.color, to_member.id);
                        if (membersByColor.indexOfKey(member.color) < 0) {
                            membersByColor.put(member.color, member.id);
                        }

                        // т.к. в calculationList приходит "кто кому должен" надо перевернуть значения, что бы получилось "кто кому дал"
                        // поэтому первым параметром в ShortCost отдаем to_member а вторым member
                        Cost forGroupCost = new ShortCost(to_member.color, member.color, calcCost.sum());
                        calcListCosts[i] = forGroupCost;
                    }

                    calculationList = Calculation.call(calcListCosts);

                    //Переводим цвета обратно в учатников что бы вывести в список
                    for (int i = 0; i < calculationList.length; i++) {

                        Cost c = new ShortCost(
                                membersByColor.get(calculationList[i].member()),
                                membersByColor.get(calculationList[i].to_member()),
                                calculationList[i].sum()
                        );

                        calculationList[i] = c;

                    }


                    //groupTimer.stop();

                }
            }


            if (calculationList.length > 0) {
                finalList = Help.concat(finalList, new Cost[]{new ShortCost(-1, -1, 0f, "↓ Кто кому сколько должен ↓")});
                finalList = Help.concat(finalList, calculationList);
            } else {
                finalList = Help.concat(finalList, new Cost[]{new ShortCost(-1, -1, 0f, "Долгов нет")});
            }

            if (costList.hasRows()) {
                finalList = Help.concat(finalList, new Cost[]{new ShortCost(-1, -1, 0f, "↓ Список всех операций ↓")});

                if (!t_settings.INSTANCE.active(NamespaceSettings.GROUP_COST)) {
                    // Если группировка не нужна выводим как есть
                    finalList = Help.concat(finalList, costList.getAllRows());
                } else {
                    // Группировка
                    Cost[] allCost = costList.getAllRows();
                    List<GroupCost> groupCostList = new ArrayList<>();
                    String lastKey = "";

                            /*
                                Предполагается, что getAllRows выдает отсортированный по дате массив поэтому дополнительная сортировка не нужна.
                                Достаточно "набивать" группу до тех пор, пока не дойдем до следующей даты, все что дальше - следующая группа
                                Для дополнительной надежности проверяется не только дата но и комментарий
                             */
                    for (Cost cost : allCost) {
                        String key = cost.date().getTime() + cost.comment();
                        if (key.equals(lastKey)) {
                            try {
                                groupCostList.get(groupCostList.size() - 1).addCost(cost);
                            } catch (Exception ex) {
                                Help.alert(ex.getMessage());
                                return null;
                            }
                        } else {
                            groupCostList.add(new GroupCost(cost));
                            lastKey = key;
                        }
                    }

                    finalList = Help.concat(finalList, groupCostList.toArray(new GroupCost[0]));
                }

            }

            //allTimer.stop();

            adapter = new AdapterCostList(MainActivity.INSTANCE.getApplicationContext(), finalList);

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        procDialog.dismiss();

        listViewCosts.setAdapter(adapter);

        if (adapter.getCount() > 5
                && !t_settings.INSTANCE.active(NamespaceSettings.DELETE_COST_SHOWED_HELP)
                && !t_settings.INSTANCE.active(NamespaceSettings.HIDE_ALL_HELP)) {
            Help.alertHelp(MainActivity.INSTANCE.getString(R.string.deleteCostHelp));

            t_settings.INSTANCE.setActive(NamespaceSettings.DELETE_COST_SHOWED_HELP, true);
        }

        //Если 10 операций уже внесено то пусть подсказки больше не показываются
        if (adapter.getCount() > 10 && !t_settings.INSTANCE.active(NamespaceSettings.HIDE_ALL_HELP)) {
            t_settings.INSTANCE.setActive(NamespaceSettings.HIDE_ALL_HELP, true);
        }

        if (t_settings.INSTANCE.active(NamespaceSettings.GROUP_COST_NEED_MESSAGE)) {
            Help.alertHelp(MainActivity.INSTANCE.getString(R.string.groupCostMessage));
            t_settings.INSTANCE.setActive(NamespaceSettings.GROUP_COST_NEED_MESSAGE, false);
        }

        if (postRunner != null){
            postRunner.run();
        }
    }

    public interface PostRunner{
        void run();
    }
}
