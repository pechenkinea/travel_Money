package com.pechenkin.travelmoney.bd.local.table.helper.migrate;

import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.CostLocal;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.CostTable;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.List;

public class Migrate {

    public static void costToTransaction(SQLiteDatabase db) {

        TripTableRow[] trips = TableTrip.INSTANCE.getAll();
        for (TripTableRow trip : trips) {
            CostLocal[] tripCosts = CostTable.INSTANCE.getAllByTripId(trip.id).getAllRows();

            List<DraftTransaction> transaction = group(tripCosts);
            //TODO Записывать в БД

        }

    }


    /**
     * группирует операции по дате и комментарию
     *
     * @param costs отсортированный по дате массив трат
     */
    public static List<DraftTransaction> group(CostLocal[] costs) {
        StreamList<DraftTransaction> groupCostList = new StreamList<>(new ArrayList<>());
        String lastKey = "";

        for (CostLocal cost : costs) {

            String key = cost.date.getTime() + cost.comment;
            if (!key.equals(lastKey)) {
                if (groupCostList.size() > 0) {
                    groupCostList.add(createDraftTransactionByCost(cost));
                }
                lastKey = key;
            }

            DraftTransactionItem draftTransactionItem = new DraftTransactionItem(cost.member, (int) (cost.sum * 100), 0);
            groupCostList.Last().addDebitItem(draftTransactionItem);
            draftTransactionItem.setChange(true); // setChange надо обязательно после addDebitItem, что бы назначился листенер изменений и пересчитался кредит

        }
        return groupCostList;
    }


    private static DraftTransaction createDraftTransactionByCost(CostLocal cost) {
        DraftTransaction result = new DraftTransaction()
                .setComment(cost.comment)
                .setDate(cost.date)
                .setImageUrl(cost.image_dir)
                .setRepayment(cost.repayment)
                .addCreditItem(
                        new DraftTransactionItem(cost.member, 0, 0)
                );
        result.setActive(cost.active);
        return result;
    }

    public static class Sumator {
        private double sumOne;
        int returnValues = 0;
        int minGroupCount = 1;
        int addOneCountInGroup;

        public Sumator(double sumOne) {
            this.sumOne = sumOne;

            double allSum = sumOne;
            while (allSum % 1 != 0) {
                allSum = sumOne * ++minGroupCount;
            }

            addOneCountInGroup = ((int) allSum) - (((int) sumOne) * minGroupCount);
        }

        public int getNext() {
            returnValues++;

            int result = (int) sumOne;
            if (returnValues <= addOneCountInGroup) {
                result++;
            }
            if (minGroupCount == returnValues)
                returnValues = 0;

            return result;

        }


    }
}
