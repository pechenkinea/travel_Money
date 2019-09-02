package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.LongSparseArray;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.LocalTransaction;
import com.pechenkin.travelmoney.bd.local.LocalTransactionItem;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<Transaction> getTransactionsByTrip(long tripId) {

        String sql = "SELECT * FROM " + Namespace.TABLE_TRANSACTION + " WHERE " + Namespace.FIELD_TRIP + " = '" + tripId + "'";

        String sqlTransactionItems = "SELECT " +
                "  i." + Namespace.FIELD_ID +
                ", i." + Namespace.FIELD_MEMBER +
                ", i." + Namespace.FIELD_CREDIT +
                ", i." + Namespace.FIELD_DEBIT +
                ", i." + Namespace.FIELD_TRANSACTION +

                " FROM " + Namespace.TABLE_TRANSACTION_ITEMS + " as i" +
                " INNER JOIN " + Namespace.TABLE_TRANSACTION + " as t ON t." + Namespace.FIELD_ID + " = i." + Namespace.FIELD_TRANSACTION +
                " WHERE t." + Namespace.FIELD_TRIP + " = '" + tripId + "';";

        LongSparseArray<LocalTransaction> localTransactionLongSparseArray = new LongSparseArray<>();

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {

            //Сначала считываем все транзакции
            try (Cursor sqlResult = db.rawQuery(sql, null)) {
                if (sqlResult.moveToFirst()) {
                    do {
                        LocalTransaction localTransaction = new LocalTransaction(sqlResult);
                        localTransactionLongSparseArray.put(localTransaction.getId(), localTransaction);
                    }
                    while (sqlResult.moveToNext());
                }
            }

            //после того. как у нас появилась мапа транзакций добавляем в нее проводки
            try (Cursor sqlResult = db.rawQuery(sqlTransactionItems, null)) {
                if (sqlResult.moveToFirst()) {
                    do {
                        LocalTransactionItem localTransactionItem = new LocalTransactionItem(sqlResult);

                        LocalTransaction transaction = localTransactionLongSparseArray.get(localTransactionItem.getTransactionId());
                        if (transaction != null) {
                            transaction.addTransactionItem(localTransactionItem);
                        }
                    }
                    while (sqlResult.moveToNext());
                }
            }
        }


        List<Transaction> result = new ArrayList<>(localTransactionLongSparseArray.size());
        for (int i = 0; i < localTransactionLongSparseArray.size(); i++) {
            result.add(localTransactionLongSparseArray.valueAt(i));
        }

        Collections.sort(result, (left, right) -> right.getDate().compareTo(left.getDate()));

        return result;

    }



    public void setTransactionState(long id, boolean active) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, active ? 1 : 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_TRANSACTION, cv, Namespace.FIELD_ID + " = " + id, null);
        }
    }
}
