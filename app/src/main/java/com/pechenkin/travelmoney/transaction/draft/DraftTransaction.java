package com.pechenkin.travelmoney.transaction.draft;

import com.pechenkin.travelmoney.Division;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftTransaction implements Transaction {

    private String comment;
    private Date date = new Date();
    private String imageUrl = "";
    private boolean repayment = false;
    private List<TransactionItem> transactionItems = new ArrayList<>();


    /**
     * обновляет суммы по всем transactionItems при изменении суммы дебета у одной
     * уравнивает дебеты и кредиты
     */
    public void updateSum() {


        int creditSum = 0;
        for (TransactionItem transactionItem : getCreditItems()) {
            creditSum += transactionItem.getCredit();
        }

        List<DaftTransactionItem> noChangeItems = new ArrayList<>();

        for (TransactionItem transactionItem : getDebitItems()) {

            if (((DaftTransactionItem) transactionItem).isChange()) {
                creditSum -= transactionItem.getDebit();
            } else {
                noChangeItems.add((DaftTransactionItem) transactionItem);
            }
        }

        if (creditSum < 0) {
            ((DaftTransactionItem) getCreditItems().get(0)).addCredit(creditSum * -1);
            for (DaftTransactionItem daftTransactionItem : noChangeItems) {
                daftTransactionItem.debit = 0;
            }
            return;
        }

        if (noChangeItems.size() > 0) {
            Division division = new Division(creditSum, noChangeItems.size());
            for (DaftTransactionItem daftTransactionItem : noChangeItems) {
                daftTransactionItem.debit = division.getNext();
            }
            return;
        }

        if (creditSum > 0) {
            ((DaftTransactionItem) getCreditItems().get(0)).addCredit(creditSum * -1);
        }


    }

    public DraftTransaction addTransactionItem(DaftTransactionItem transactionItem) {
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
    public List<TransactionItem> getDebitItems() {

        List<TransactionItem> result = new ArrayList<>();
        for (TransactionItem transactionItem : transactionItems) {
            if (transactionItem.getDebit() > 0) {
                result.add(transactionItem);
            }
        }
        return result;
    }

    @Override
    public List<TransactionItem> getCreditItems() {
        List<TransactionItem> result = new ArrayList<>();
        for (TransactionItem transactionItem : transactionItems) {
            if (transactionItem.getCredit() > 0) {
                result.add(transactionItem);
            }
        }
        return result;
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
