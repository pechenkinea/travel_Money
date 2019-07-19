package com.pechenkin.travelmoney.page;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.CostBaseTableRow;
import com.pechenkin.travelmoney.list.AdapterSumResultList;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.summry.Summary;
import com.pechenkin.travelmoney.summry.Total;

/**
 * Created by pechenkin on 19.04.2018.]
 * Страница "Статистика"
 */

public class SumResultListPage extends ListPage {


    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_more).getParam());
    }

    @Override
    protected int getPageId() {
        return R.layout.view_list_sum_result;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.statistic) +  "(" + t_trips.ActiveTrip.name + ")";
    }

    @Override
    public void addEvents() {
        super.addEvents();
    }

    @Override
    protected boolean fillFields() {
        if (t_trips.ActiveTrip == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }


        CostQueryResult allCostTrip = t_costs.getAllByTripId(t_trips.ActiveTrip.id);
        ListView list1 =  MainActivity.INSTANCE.findViewById(getListViewId());
        if (!allCostTrip.hasRows())
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            list1.setAdapter(null);
            return false;
        }
        else
        {
            CostBaseTableRow[] costs = allCostTrip.getAllRows();
            Summary[] summary = Total.getSummary(costs);
            AdapterSumResultList adapter = new AdapterSumResultList(MainActivity.INSTANCE, summary);
            list1.setAdapter(adapter);
        }
        return true;
    }

    @Override
    protected int getListViewId() {
        return R.id.list_sum_result;
    }

    @Override
    protected void onItemClick(ListView list, AdapterView<?> a, View view, int position, long id) {

    }

    @Override
    protected boolean onItemLongClick(ListView list, AdapterView<?> a, View view, int position, long arg3) {
        return false;
    }


}
