package com.pechenkin.travelmoney.page.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.OnlySumCostItem;
import com.pechenkin.travelmoney.cost.TotalItemDiagram;
import com.pechenkin.travelmoney.cost.calculation.Calculation;
import com.pechenkin.travelmoney.cost.GroupCost;
import com.pechenkin.travelmoney.cost.adapter.LabelItem;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.summry.Summary;
import com.pechenkin.travelmoney.summry.Total;

public class CostListBackground extends AsyncTask<Void, Void, Void> {

    private final TripBaseTableRow trip;
    private ProgressDialog processDialog;
    private CostListItem[] finalList = {};

    private final DoOnPostExecute doOnPostExecute;

    public CostListBackground(TripBaseTableRow trip, DoOnPostExecute doOnPostExecute) {
        this.trip = trip;
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

            CostQueryResult costList = t_costs.getAllByTripId(this.trip.id);

            ShortCost[] calculationList = {};
            if (costList.hasRows()) {
                calculationList = Calculation.calculate(costList.getAllRows());

                // Группируем, если есть группировка по цветам
                if (t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)) {
                    calculationList = Calculation.groupByColor(calculationList);
                }
            }

            if (calculationList.length > 0) {

                finalList = Help.concat(finalList, new CostListItem[]{new LabelItem("Кто кому сколько должен")});
                finalList = Help.concat(finalList, calculationList);
            } else {
                finalList = Help.concat(finalList, new CostListItem[]{new LabelItem("Долгов нет")});
            }

            if (costList.hasRows()) {


                //TODO не пробегать по всем операциям несколько раз. все сделавть в одном цикле
                double allSum = 0;
                for (Cost cost : costList.getAllRows()) {
                    if (cost.isActive() != 0){
                        allSum += cost.getSum();
                    }
                }

                Summary[] summary = Total.getSummary(costList.getAllRows());



                finalList = Help.concat(new CostListItem[]{ new TotalItemDiagram(allSum, summary)}, finalList);

                finalList = Help.concat(finalList, new CostListItem[]{
                        new LabelItem("Список всех операций")
                });


                if (t_settings.INSTANCE.active(NamespaceSettings.GROUP_COST)) {
                    // Группировка
                    GroupCost[] groupCostList = GroupCost.group(costList.getAllRows());
                    if (groupCostList != null) {
                        finalList = Help.concat(finalList, groupCostList);
                    }
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
