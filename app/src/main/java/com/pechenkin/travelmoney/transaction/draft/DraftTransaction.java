package com.pechenkin.travelmoney.transaction.draft;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.Division;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DraftTransaction implements Transaction {

    private String comment = "";
    private boolean isActive = true;
    private Date date = new Date();
    private String imageUrl = "";
    private boolean repayment = false;
    private StreamList<TransactionItem> creditItems = new StreamList<>(new ArrayList<>());
    private StreamList<TransactionItem> debitItems = new StreamList<>(new ArrayList<>());

    private DraftUpdateListener draftUpdateListener = null;

    public void setDraftUpdateListener(DraftUpdateListener draftUpdateListener) {
        this.draftUpdateListener = draftUpdateListener;
    }

    public DraftUpdateListener getDraftUpdateListener() {
        return draftUpdateListener;
    }

    public void validate() throws ValidateException {

        if (getSum() == 0) {
            throw new ValidateException("Введите сумму");
        }
        if (getComment().length() == 0) {
            throw new ValidateException("Введите комментарий");
        }

        int[] debitSum = new int[]{0};
        getDebitItems().ForEach(transactionItem -> debitSum[0] += transactionItem.getDebit());

        if (debitSum[0] == 0) {
            throw new ValidateException("Выберите участников");
        }


        int[] creditSum = new int[]{0};
        getCreditItems().ForEach(transactionItem -> creditSum[0] += transactionItem.getCredit());


        if (creditSum[0] != debitSum[0]) {
            throw new ValidateException("Кредит и дебет имеют разные суммы");
        }

    }

    /**
     * обновляет суммы по всем transactionItems при изменении суммы дебета у одной.
     * Уравнивает дебеты и кредиты
     */
    public void updateSum() {
        final int[] creditSum = new int[]{0};

        getCreditItems().ForEach(transactionItem -> {
            creditSum[0] += transactionItem.getCredit();
        });


        List<DraftTransactionItem> noChangeItems = new ArrayList<>();

        getDebitItems().ForEach(transactionItem -> {
            if (((DraftTransactionItem) transactionItem).isChange()) {
                creditSum[0] -= transactionItem.getDebit();
            } else {
                noChangeItems.add((DraftTransactionItem) transactionItem);
            }
        });


        if (creditSum[0] < 0) {
            ((DraftTransactionItem) getCreditItems().get(0)).addCredit(creditSum[0] * -1);
            for (DraftTransactionItem draftTransactionItem : noChangeItems) {
                draftTransactionItem.debit = 0;
            }
            return;
        }

        if (noChangeItems.size() > 0) {
            Division division = new Division(creditSum[0], noChangeItems.size());
            for (DraftTransactionItem draftTransactionItem : noChangeItems) {
                draftTransactionItem.debit = division.getNext();
            }
            return;
        }

        if (creditSum[0] > 0) {

            DraftTransactionItem creditItem = (DraftTransactionItem) getCreditItems().get(0);

            if (creditItem.getCredit() > creditSum[0]) {
                creditItem.addCredit(creditSum[0] * -1);
            }
        }
    }

    public void update() {

        updateSum();
        if (getDebitItems().size() == 0) { //На момент, пока внесен только кредит пересчет не нужен
            return;
        }


        if (draftUpdateListener != null) {
            draftUpdateListener.update();
        }


    }

    public DraftTransaction addCreditItem(DraftTransactionItem transactionItem) {
        if (transactionItem.getDebit() > 0)
            throw new IllegalArgumentException("Нельзя в кредит добавлять дебетовые элементы");

        creditItems.add(transactionItem);
        transactionItem.setUpdateListener(this::update);
        return this;
    }

    public DraftTransaction addDebitItem(DraftTransactionItem transactionItem) {
        if (transactionItem.getCredit() > 0)
            throw new IllegalArgumentException("Нельзя в дебет добавлять кредитовые элементы");

        debitItems.add(transactionItem);
        transactionItem.setUpdateListener(this::update);
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
        return debitItems;
    }

    @Override
    public StreamList<TransactionItem> getCreditItems() {
        return creditItems;
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean value) {
        isActive = value;
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
