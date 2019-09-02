package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.TransactionTable;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class LocalTransaction extends IdTableRow implements Transaction {

    private final String comment;
    private final String imageUrl;
    private final Date date;
    private boolean active;
    private boolean repayment;

    private StreamList<TransactionItem> allItems = new StreamList<>(new ArrayList<>());

    public LocalTransaction(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.imageUrl = getStringColumnValue(Namespace.FIELD_IMAGE_DIR, c);
        this.date = getDateColumnValue(Namespace.FIELD_DATE, c);

        this.active = getIntColumnValue(Namespace.FIELD_ACTIVE, c) != 0;
        this.repayment = getIntColumnValue(Namespace.FIELD_REPAYMENT, c) != 0;

    }

    public void addTransactionItem(TransactionItem transactionItem){
        allItems.add(transactionItem);
    }

    @Override
    public long getId() {
        return this.id;
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
        return allItems.Filter(transactionItem -> transactionItem.getCredit() == 0);
    }

    @Override
    public StreamList<TransactionItem> getCreditItems() {
        return allItems.Filter(transactionItem -> transactionItem.getDebit() == 0);
    }

    @Override
    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean value) {
        TransactionTable.INSTANCE.setTransactionState(this.id, value);
        this.active = value;
    }

    @Override
    public int getSum() {
        int[] sum = new int[]{0};
        getCreditItems().ForEach(transactionItem -> sum[0]+= transactionItem.getCredit());
        return sum[0];
    }

    @Override
    public boolean isRepayment() {
        return this.repayment;
    }
}
