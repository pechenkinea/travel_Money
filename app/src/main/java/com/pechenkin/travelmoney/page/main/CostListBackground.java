package com.pechenkin.travelmoney.page.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.t_settings;
import com.pechenkin.travelmoney.cost.Cost;
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
import com.pechenkin.travelmoney.diagram.DefaultDiagram;
import com.pechenkin.travelmoney.diagram.Diagram;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.master.MasterCostInfo;

import java.util.ArrayList;
import java.util.List;

public class CostListBackground extends AsyncTask<Void, Void, Void> {

    private final Trip trip;
    private ProgressDialog processDialog;
    private List<CostListItem> finalList = new ArrayList<>();
    private boolean readOnly;

    private final DoOnPostExecute doOnPostExecute;

    public CostListBackground(boolean readOnly, Trip trip, DoOnPostExecute doOnPostExecute) {
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

            List<Cost> costList = this.trip.getAllCost();

            List<ShortCost> calculationList;

            if (costList.size() > 0) {

                Calculation calc = new Calculation(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));
                Total total = new Total();
                AllSum allSumIteration = new AllSum();

                ProcessIterate.doIterate(costList, new CostIterable[]{calc, total, allSumIteration});

                calculationList = calc.getResult();
                List<Total.MemberSum> totalResult = total.getResult();
                int allSum = allSumIteration.getSum();


                Diagram diagram = DefaultDiagram.createDefaultDiagram(allSum, totalResult);

                if (!this.readOnly) {
                    diagram.setOnDiagramSelectItem(itemId -> {

                        PageParam param = new PageParam.BuildingPageParam()
                                .setMember(itemId)
                                .setBackPage(MainPage.class)
                                .getParam();
                        PageOpener.INSTANCE.open(MasterCostInfo.class, param);

                    });
                }


                finalList.add(diagram);


            } else {
                calculationList = new ArrayList<>();
            }

            if (calculationList.size() > 0) {
                finalList.add(new LabelItemWithMenu("Кто кому сколько должен"));
                finalList.addAll(calculationList);
            } else {
                finalList.add(new LabelItemWithMenu("Долгов нет"));
            }

            if (costList.size() > 0) {

                finalList.add(new LabelItem("Список всех операций"));
                // Группировка
                List<GroupCost> groupCostList = GroupCost.group(costList);
                finalList.addAll(groupCostList);
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
        void onPostExecute(List<CostListItem> finalList);
    }


}
