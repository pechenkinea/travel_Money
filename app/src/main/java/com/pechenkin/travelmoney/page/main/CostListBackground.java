package com.pechenkin.travelmoney.page.main;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.SettingsTable;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.transaction.list.LabelItem;
import com.pechenkin.travelmoney.transaction.list.LabelItemWithMenu;
import com.pechenkin.travelmoney.transaction.list.TotalItem;
import com.pechenkin.travelmoney.transaction.list.TransactionListItem;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;
import com.pechenkin.travelmoney.transaction.processing.ProcessIterate;
import com.pechenkin.travelmoney.transaction.processing.calculation.Calculation;
import com.pechenkin.travelmoney.transaction.processing.summary.AllSum;
import com.pechenkin.travelmoney.transaction.processing.summary.Total;
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

            List<Transaction> costList = this.trip.getTransactions();

            List<TotalItem> calculationList;

            if (costList.size() > 0) {

                Calculation calc = new Calculation(SettingsTable.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));
                Total total = new Total();
                AllSum allSumIteration = new AllSum();

                ProcessIterate.doIterate(costList, new CostIterable[]{calc, total, allSumIteration});

                calculationList = calc.getResult();
                List<Total.MemberSum> totalResult = total.getResult();
                int allSum = allSumIteration.getSum();


                Diagram diagram = DefaultDiagram.createDefaultDiagram(allSum, totalResult);

                if (!this.readOnly) {
                    diagram.setOnDiagramSelectItem(itemId -> {

                        DraftTransaction draftTransaction = new DraftTransaction()
                                .addTransactionItem(new DraftTransactionItem(itemId, 0, 1));

                        PageParam param = new PageParam().setDraftTransaction(draftTransaction);
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
                List<TransactionListItem> groupCostList = TransactionListItem.create(costList);
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
