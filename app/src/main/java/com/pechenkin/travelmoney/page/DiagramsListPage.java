package com.pechenkin.travelmoney.page;

import android.app.AlertDialog;
import android.widget.ListView;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.TableSettings;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.AdapterCostList;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.transaction.list.LabelItem;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;
import com.pechenkin.travelmoney.transaction.processing.ProcessIterate;
import com.pechenkin.travelmoney.transaction.processing.summary.AllSum;
import com.pechenkin.travelmoney.transaction.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.DiagramName;
import com.pechenkin.travelmoney.diagram.OnDiagramSelect;
import com.pechenkin.travelmoney.diagram.impl.BarDiagram;
import com.pechenkin.travelmoney.diagram.impl.DebitCreditDiagram;
import com.pechenkin.travelmoney.diagram.impl.LineDiagram;
import com.pechenkin.travelmoney.diagram.impl.TotalItemDiagram;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pechenkin on 19.04.2018.]
 * Страница "Статистика"
 */

public class DiagramsListPage extends ListPage {


    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_more));
    }

    @Override
    protected int getPageId() {
        return R.layout.view_list_sum_result;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.diagrams) + "(" + TripManager.INSTANCE.getActiveTrip().getName() + ")";
    }


    @Override
    protected boolean fillFields() {


        List<Transaction> allTransactionsTrip = TripManager.INSTANCE.getActiveTrip().getTransactions();
        ListView list1 = MainActivity.INSTANCE.findViewById(getListViewId());
        if (allTransactionsTrip.size() == 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            list1.setAdapter(null);
            return false;
        } else {
            Total total = new Total();
            AllSum allSumIteration = new AllSum();

            ProcessIterate.doIterate(allTransactionsTrip, new CostIterable[]{total, allSumIteration});

            List<Total.MemberSum> totalResult = total.getResult();
            int allSum = allSumIteration.getSum();

            if (totalResult.size() == 0){
                Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
                list1.setAdapter(null);
                return false;
            }

            OnDiagramSelect onDiagramSelect = diagram -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                builder.setTitle("")
                        .setMessage("Выбрать эту диаграмму для отображения в списке трат?")
                        .setCancelable(true)

                        .setPositiveButton("Да", (dialog, which) -> {
                            dialog.cancel();

                            DiagramName diagramName = diagram.getClass().getAnnotation(DiagramName.class);
                            if (diagramName != null) {
                                TableSettings.INSTANCE.set(NamespaceSettings.LIKE_DIAGRAM_NAME, diagramName.name());
                                Help.message("Успешно");
                            } else {
                                Help.message("Ошибка");
                            }

                        })
                        .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();
            };

            List<CostListItem> listItems = new ArrayList<>();

            listItems.add(new LabelItem("Траты"));
            listItems.add(new TotalItemDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect));
            listItems.add(new BarDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect));
            listItems.add(new LineDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect));
            listItems.add(new LabelItem("Долги"));
            listItems.add(new DebitCreditDiagram(allSum, totalResult).setOnDiagramSelect(onDiagramSelect));


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
