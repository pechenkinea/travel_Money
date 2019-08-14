package com.pechenkin.travelmoney.page.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.query.QueryResult;
import com.pechenkin.travelmoney.bd.table.query.row.CostTableRow;
import com.pechenkin.travelmoney.bd.table.query.row.TripTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.LabelItem;
import com.pechenkin.travelmoney.cost.adapter.LabelItemWithMenu;
import com.pechenkin.travelmoney.cost.group.GroupCost;
import com.pechenkin.travelmoney.cost.processing.CostIterable;
import com.pechenkin.travelmoney.cost.processing.ProcessIterate;
import com.pechenkin.travelmoney.cost.processing.calculation.Calculation;
import com.pechenkin.travelmoney.cost.processing.summary.AllSum;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.BarDiagram;
import com.pechenkin.travelmoney.diagram.TotalItemDiagram;

public class CostListBackground extends AsyncTask<Void, Void, Void> {

    private final TripTableRow trip;
    private ProgressDialog processDialog;
    private CostListItem[] finalList = {};
    private boolean readOnly;

    private final DoOnPostExecute doOnPostExecute;

    public CostListBackground(boolean readOnly, TripTableRow trip, DoOnPostExecute doOnPostExecute) {
        this.trip = trip;
        this.readOnly = readOnly;
        this.doOnPostExecute = doOnPostExecute;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        processDialog = Help.createProgressDialog(MainActivity.INSTANCE);
        processDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        if (this.trip != null) {

            QueryResult<CostTableRow> costList = t_costs.getAllByTripId(this.trip.id);

            ShortCost[] calculationList;
            Total.MemberSum[] totalResult;
            double allSum = 0;

            if (costList.hasRows()) {

                Calculation calc = new Calculation(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));
                Total total = new Total();
                AllSum allSumIteration = new AllSum();

                ProcessIterate.doIterate(costList.getAllRows(), new CostIterable[]{calc, total, allSumIteration});

                calculationList = calc.getResult();
                totalResult = total.getResult();
                allSum = allSumIteration.getSum();
            } else {
                calculationList = new ShortCost[0];
                totalResult = new Total.MemberSum[0];
            }

            if (calculationList.length > 0) {
                finalList = Help.concat(finalList, new CostListItem[]{new LabelItemWithMenu("Кто кому сколько должен")});
                finalList = Help.concat(finalList, calculationList);
            } else {
                finalList = Help.concat(finalList, new CostListItem[]{new LabelItem("Долгов нет")});
            }

            if (costList.hasRows()) {

                finalList = Help.concat(new CostListItem[]{
                        new TotalItemDiagram(allSum, totalResult, this.readOnly),
                        new BarDiagram(totalResult)
                }, finalList);

                finalList = Help.concat(finalList, new CostListItem[]{new LabelItem("Список всех операций")});


                if (t_settings.INSTANCE.active(NamespaceSettings.GROUP_COST)) {
                    // Группировка
                    GroupCost[] groupCostList = GroupCost.group(costList.getAllRows());
                    finalList = Help.concat(finalList, groupCostList);
                } else {
                    // Если группировка не нужна выводим как есть
                    finalList = Help.concat(finalList, costList.getAllRows());
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        processDialog.dismiss();

        if (doOnPostExecute != null) {
            doOnPostExecute.onPostExecute(finalList);
        }
    }

    public interface DoOnPostExecute {
        void onPostExecute(CostListItem[] finalList);
    }


}
