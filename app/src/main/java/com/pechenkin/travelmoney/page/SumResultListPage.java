package com.pechenkin.travelmoney.page;

import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.QueryResult;
import com.pechenkin.travelmoney.bd.table.query.row.CostTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.adapter.AdapterCostList;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.processing.CostIterable;
import com.pechenkin.travelmoney.cost.processing.ProcessIterate;
import com.pechenkin.travelmoney.cost.processing.summary.AllSum;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.BarDiagram;
import com.pechenkin.travelmoney.diagram.LineDiagram;
import com.pechenkin.travelmoney.diagram.TotalItemDiagram;
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
        return MainActivity.INSTANCE.getString(R.string.statistic) + "(" + t_trips.getActiveTrip().name + ")";
    }


    @Override
    protected boolean fillFields() {
        if (t_trips.getActiveTrip() == null) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }


        QueryResult<CostTableRow> allCostTrip = t_costs.getAllByTripId(t_trips.getActiveTrip().id);
        ListView list1 = MainActivity.INSTANCE.findViewById(getListViewId());
        if (!allCostTrip.hasRows()) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            list1.setAdapter(null);
            return false;
        } else {
            Total total = new Total();
            AllSum allSumIteration = new AllSum();

            ProcessIterate.doIterate(allCostTrip.getAllRows(), new CostIterable[]{total, allSumIteration});

            Total.MemberSum[] totalResult = total.getResult();
            double allSum = allSumIteration.getSum();

            CostListItem[] listItems = new CostListItem[]{
                    new TotalItemDiagram(allSum, totalResult),
                    new BarDiagram(totalResult),
                    new LineDiagram(allSum, totalResult)
            };

            AdapterCostList adapter = new AdapterCostList(MainActivity.INSTANCE.getApplicationContext(), listItems);

            //AdapterSumResultList adapter = new AdapterSumResultList(MainActivity.INSTANCE, totalResult);
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
