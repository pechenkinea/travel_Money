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

    public CostListBackground(ListView listViewCosts, TripBaseTableRow trip) {
        this.trip = trip;
        this.listViewCosts = listViewCosts;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        procDialog = Help.createProgressDialog(MainActivity.INSTANCE);
        procDialog.show();

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

            adapter = new AdapterCostList(MainActivity.INSTANCE.getApplicationContext(), finalList);

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        listViewCosts.setAdapter(adapter);
        procDialog.dismiss();

        if (adapter.getCount() > 5 && !t_settings.INSTANCE.active(NamespaceSettings.DELETE_COST_SHOWED_HELP)) {
            Help.alertHelp(MainActivity.INSTANCE.getString(R.string.deleteCostHelp));

            t_settings.INSTANCE.setActive(NamespaceSettings.DELETE_COST_SHOWED_HELP, true);
        }

    }

}
