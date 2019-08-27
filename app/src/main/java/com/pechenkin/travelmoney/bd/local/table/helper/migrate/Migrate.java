package com.pechenkin.travelmoney.bd.local.table.helper.migrate;

import com.pechenkin.travelmoney.bd.local.CostLocal;
import com.pechenkin.travelmoney.bd.local.TripLocal;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.CostTable;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migrate {

    public static void costToTransaction() {

        TripTableRow[] trips = TableTrip.INSTANCE.getAll();
        for (TripTableRow trip : trips) {
            CostLocal[] tripCosts = CostTable.INSTANCE.getAllByTripId(trip.id).getAllRows();

            StreamList<DraftTransaction> transaction = new StreamList<>(group(tripCosts));

            TripLocal tripLocal = new TripLocal(trip);

            transaction.ForEach(tripLocal::addTransaction);
        }

    }


    /**
     * группирует операции по дате и комментарию
     *
     * @param costs отсортированный по дате массив трат
     */
    private static List<DraftTransaction> group(CostLocal[] costs) {
        StreamList<DraftTransaction> groupCostList = new StreamList<>(new ArrayList<>());
        String lastKey = "";

        Map<String, Sumator> sumatorMap = new HashMap<>();
        for (CostLocal cost : costs) {

            if (cost.sum == 0) {
                continue;
            }

            String key = cost.date.getTime() + cost.comment;
            if (!key.equals(lastKey)) {

                groupCostList.add(createDraftTransactionByCost(cost));
                sumatorMap = new HashMap<>();

                lastKey = key;
            }

            //перевод в копейки и округление
            double s = (double) ((long) (cost.sum * 100 * 100_000)) / 100_000;
            Sumator sumator = sumatorMap.get("" + s);
            if (sumator == null) {

                sumator = new Sumator(s);
                sumatorMap.put("" + s, sumator);
            }

            DraftTransactionItem draftTransactionItem = new DraftTransactionItem(cost.to_member, sumator.getNext(), 0);
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

            double allSum = this.sumOne;
            while (allSum % 1 != 0 && allSum % 1 < 0.99 && minGroupCount < 100) {
                allSum = this.sumOne * ++minGroupCount;
            }

            addOneCountInGroup = ((int) Math.round(allSum)) - (((int) this.sumOne) * minGroupCount);
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
