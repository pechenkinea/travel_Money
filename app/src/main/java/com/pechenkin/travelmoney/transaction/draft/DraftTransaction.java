package com.pechenkin.travelmoney.transaction.draft;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.Division;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftTransaction implements Transaction {

    private String comment;
    private Date date = new Date();
    private String imageUrl = "";
    private boolean repayment = false;
    private StreamList<TransactionItem> transactionItems = new StreamList<>(new ArrayList<>());

    public void validate() throws ValidateException {

        int[] debitSum = new int[]{0};
        getDebitItems().ForEach(transactionItem -> debitSum[0] += transactionItem.getDebit());

        if (debitSum[0] == 0){
            throw new ValidateException("Выберите участников");
        }


        int[] creditSum = new int[]{0};
        getCreditItems().ForEach(transactionItem -> creditSum[0] += transactionItem.getCredit());


        if (creditSum[0] != debitSum[0]){
            throw new ValidateException("Кредит и дебет имеют разные суммы");
        }

    }

    /**
     * обновляет суммы по всем transactionItems при изменении суммы дебета у одной.
     * Уравнивает дебеты и кредиты
     */
    public void updateSum() {


        if (getDebitItems().size() == 0) { //На момент, пока внесен только кредит пересчет не нужен
            return;
        }

        int creditSum = 0;
        for (TransactionItem transactionItem : getCreditItems()) {
            creditSum += transactionItem.getCredit();
        }

        List<DraftTransactionItem> noChangeItems = new ArrayList<>();

        for (TransactionItem transactionItem : getDebitItems()) {

            if (((DraftTransactionItem) transactionItem).isChange()) {
                creditSum -= transactionItem.getDebit();
            } else {
                noChangeItems.add((DraftTransactionItem) transactionItem);
            }
        }

        if (creditSum < 0) {
            ((DraftTransactionItem) getCreditItems().get(0)).addCredit(creditSum * -1);
            for (DraftTransactionItem draftTransactionItem : noChangeItems) {
                draftTransactionItem.debit = 0;
            }
            return;
        }

        if (noChangeItems.size() > 0) {
            Division division = new Division(creditSum, noChangeItems.size());
            for (DraftTransactionItem draftTransactionItem : noChangeItems) {
                draftTransactionItem.debit = division.getNext();
            }
            return;
        }

        if (creditSum > 0) {

            DraftTransactionItem creditItem = (DraftTransactionItem) getCreditItems().get(0);

            if (creditItem.getCredit() > creditSum) {
                creditItem.addCredit(creditSum * -1);
            }
        }


    }

    public DraftTransaction addTransactionItem(DraftTransactionItem transactionItem) {
        transactionItems.add(transactionItem);
        transactionItem.setUpdateListener(this::updateSum);
        return this;
    }


    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public StreamList<TransactionItem> getDebitItems() {
        return transactionItems.Filter(transactionItem ->
                transactionItem.getDebit() >= 0 && transactionItem.getCredit() == 0
        );
    }

    @Override
    public StreamList<TransactionItem> getCreditItems() {

        if (transactionItems.size() == 1 && transactionItems.get(0).getDebit() == 0) {
            StreamList<TransactionItem> result = new StreamList<>(new ArrayList<>(1));
            result.add(transactionItems.get(0));
            return result;
        }

        return transactionItems.Filter(transactionItem ->
                transactionItem.getCredit() > 0
        );
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setActive(boolean value) {

    }

    @Override
    public int getSum() {

        int sum = 0;
        for (TransactionItem transactionItem : getCreditItems()) {
            sum += transactionItem.getCredit();
        }

        return sum;
    }

    @Override
    public boolean isRepayment() {
        return this.repayment;
    }


    public DraftTransaction setRepayment(boolean repayment) {
        this.repayment = repayment;
        return this;
    }

    public DraftTransaction setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public DraftTransaction setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public DraftTransaction setDate(Date date) {
        this.date = date;
        return this;
    }
}
