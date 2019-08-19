package com.pechenkin.travelmoney.page;

import android.app.AlertDialog;
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
import com.pechenkin.travelmoney.diagram.DebitCreditDiagram;
import com.pechenkin.travelmoney.diagram.LineDiagram;
import com.pechenkin.travelmoney.diagram.OnDiagramSelect;
import com.pechenkin.travelmoney.diagram.TotalItemDiagram;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 19.04.2018.]
 * Страница "Статистика"
 */

public class DiagramsListPage extends ListPage {


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
        return MainActivity.INSTANCE.getString(R.string.diagrams) + "(" + t_trips.getActiveTrip().name + ")";
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

            OnDiagramSelect onDiagramSelect = diagram -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                builder.setTitle("")
                        .setMessage("Выбрать эту диаграмму для отображения в списке трат?")
                        .setCancelable(true)

                        .setPositiveButton("Да", (dialog, which) -> {
                            dialog.cancel();
                            //TODO доделать выбор диаграммы по умолчанию
                            Help.alert("не реализовано");
                        })
                        .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();
            };

            CostListItem[] listItems = new CostListItem[]{
                    new TotalItemDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect),
                    new BarDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect),
                    new LineDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect),
                    new DebitCreditDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect)
            };

            AdapterCostList adapter = new AdapterCostList(MainActivity.INSTANCE.getApplicationContext(), listItems);
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
