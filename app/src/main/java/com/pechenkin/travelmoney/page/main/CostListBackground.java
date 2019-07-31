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
import com.pechenkin.travelmoney.calculation.Calculation;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.GroupCost;
import com.pechenkin.travelmoney.cost.ShortCost;

public class CostListBackground extends AsyncTask<Void, Void, Void> {

    private final TripBaseTableRow trip;
    private ProgressDialog processDialog;
    private Cost[] finalList = {};

    public Cost[] getFinalList() {
        return finalList;
    }

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

            Cost[] calculationList = {};
            if (costList.hasRows()) {
                calculationList = Calculation.calculate(costList.getAllRows());

                // Группируем, если есть группировка по цветам
                if (t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)) {
                    calculationList = Calculation.groupByColor(calculationList);
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

        if (doOnPostExecute != null){
            doOnPostExecute.onPostExecute(finalList);
        }
    }

    public interface DoOnPostExecute{
        void onPostExecute(Cost[] finalList);
    }


}
