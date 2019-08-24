package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.TransactionTable;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LocalTransaction extends IdTableRow implements Transaction {

    private final String comment;
    private final String imageUrl;
    private final Date date;
    private boolean active;
    private boolean repayment;

    public LocalTransaction(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.imageUrl = getStringColumnValue(Namespace.FIELD_IMAGE_DIR, c);
        this.date = getDateColumnValue(Namespace.FIELD_DATE, c);

        this.active = getIntColumnValue(Namespace.FIELD_ACTIVE, c) != 0;
        this.repayment = getIntColumnValue(Namespace.FIELD_REPAYMENT, c) != 0;
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
    public List<TransactionItem> getItems() {
        TransactionItem[] items = TransactionTable.INSTANCE.getTransactionItemByTransaction(getId());
        return new ArrayList<>(Arrays.asList(items));
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
    public int getSum() {
        int sum = 0;
        for (TransactionItem item : getItems()) {
            sum += item.getCredit();
        }
        return sum;
    }

    @Override
    public boolean isRepayment() {
        return this.repayment;
    }
}
