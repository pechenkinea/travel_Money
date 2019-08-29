package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.LocalTransaction;
import com.pechenkin.travelmoney.bd.local.LocalTransactionItem;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

public class TransactionTable {

    public static TransactionTable INSTANCE = new TransactionTable();

    private TransactionTable() {

    }

    public Transaction addTransaction(long tripId, DraftTransaction draftTransaction) {

        long transactionId;

        ContentValues transaction = new ContentValues();
        transaction.put(Namespace.FIELD_COMMENT, draftTransaction.getComment());
        transaction.put(Namespace.FIELD_IMAGE_DIR, draftTransaction.getImageUrl());
        transaction.put(Namespace.FIELD_ACTIVE, 1);
        transaction.put(Namespace.FIELD_TRIP, tripId);
        transaction.put(Namespace.FIELD_DATE, draftTransaction.getDate().getTime());
        transaction.put(Namespace.FIELD_REPAYMENT, draftTransaction.isRepayment() ? 1 : 0);


        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            db.beginTransaction();

            try {
                transactionId = db.insert(Namespace.TABLE_TRANSACTION, null, transaction);

                StreamList.ForEach<TransactionItem> transactionItemForEach = transactionItem -> {

                    ContentValues cv = new ContentValues();
                    cv.put(Namespace.FIELD_MEMBER, transactionItem.getMember().getId());
                    cv.put(Namespace.FIELD_DEBIT, transactionItem.getDebit());
                    cv.put(Namespace.FIELD_CREDIT, transactionItem.getCredit());
                    cv.put(Namespace.FIELD_TRANSACTION, transactionId);

                    db.insert(Namespace.TABLE_TRANSACTION_ITEMS, null, cv);

                };

                draftTransaction.getCreditItems().ForEach(transactionItemForEach);
                draftTransaction.getDebitItems().Filter(t -> t.getDebit() > 0).ForEach(transactionItemForEach);

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }


        }

        return getTransactionById(transactionId);


    }

    private Transaction getTransactionById(long transactionId) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRANSACTION +
                " WHERE " + Namespace.FIELD_ID + " = '" + transactionId + "';";

        return new QueryResult<>(sql, LocalTransaction.class).getFirstRow();
    }

    public Transaction[] getTransactionsByTrip(long tripId) {

        String sql = "SELECT * FROM " + Namespace.TABLE_TRANSACTION +
                " WHERE " + Namespace.FIELD_TRIP + " = '" + tripId + "'" +
                " ORDER BY " + Namespace.FIELD_DATE + " DESC";

        return new QueryResult<>(sql, LocalTransaction.class).getAllRows();
    }

    public TransactionItem[] getTransactionItemByTransaction(long transactionId) {

        String sql = "SELECT * FROM " + Namespace.TABLE_TRANSACTION_ITEMS +
                " WHERE " + Namespace.FIELD_TRANSACTION + " = '" + transactionId + "';";

        return new QueryResult<>(sql, LocalTransactionItem.class).getAllRows();
    }

    public void setTransactionState(long id, boolean active) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, active ? 1 : 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_TRANSACTION, cv, Namespace.FIELD_ID + " = " + id, null);
        }
    }
}
