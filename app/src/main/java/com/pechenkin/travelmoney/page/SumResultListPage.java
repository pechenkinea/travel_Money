package com.pechenkin.travelmoney.page;

import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.query.row.CostTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.processing.CostIterable;
import com.pechenkin.travelmoney.cost.processing.ProcessIterate;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.list.AdapterSumResultList;
import com.pechenkin.travelmoney.page.main.MainPage;

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
        return MainActivity.INSTANCE.getString(R.string.statistic) +  "(" + t_trips.getActiveTrip().name + ")";
    }


    @Override
    protected boolean fillFields() {
        if (t_trips.getActiveTrip() == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }


        BaseQueryResult<CostTableRow> allCostTrip = t_costs.getAllByTripId(t_trips.getActiveTrip().id);
        ListView list1 =  MainActivity.INSTANCE.findViewById(getListViewId());
        if (!allCostTrip.hasRows())
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            list1.setAdapter(null);
            return false;
        }
        else
        {
            Total total = new Total();
            ProcessIterate.doIterate(allCostTrip.getAllRows(), new CostIterable[]{total});
            Total.MemberSum[] totalResult = total.getResult();

            AdapterSumResultList adapter = new AdapterSumResultList(MainActivity.INSTANCE, totalResult);
            list1.setAdapter(adapter);
        }
        return true;
    }

    @Override
    protected int getListViewId() {
        return R.id.list_sum_result;
    }

    @Override
    protected void onItemClick(ListView list, int position) {

    }



}
