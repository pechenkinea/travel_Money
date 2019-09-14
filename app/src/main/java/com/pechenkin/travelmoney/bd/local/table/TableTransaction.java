package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.LocalTransaction;
import com.pechenkin.travelmoney.bd.local.LocalTransactionItem;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableTransaction {

    public static final TableTransaction INSTANCE = new TableTransaction();

    private TableTransaction() {

    }

    public Transaction addTransaction(String tripUuid, Transaction draftTransaction) {

        ContentValues transaction = new ContentValues();
        transaction.put(Namespace.FIELD_COMMENT, draftTransaction.getComment());
        transaction.put(Namespace.FIELD_UUID, draftTransaction.getUuid());
        transaction.put(Namespace.FIELD_IMAGE_DIR, draftTransaction.getImageUrl());
        transaction.put(Namespace.FIELD_ACTIVE, 1);
        transaction.put(Namespace.FIELD_TRIP_UUID, tripUuid);
        transaction.put(Namespace.FIELD_DATE, draftTransaction.getDate().getTime());
        transaction.put(Namespace.FIELD_REPAYMENT, draftTransaction.isRepayment() ? 1 : 0);


        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            db.beginTransaction();

            try {
                db.insert(Namespace.TABLE_TRANSACTION, null, transaction);

                StreamList.ForEach<TransactionItem> transactionItemForEach = transactionItem -> {

                    ContentValues cv = new ContentValues();
                    cv.put(Namespace.FIELD_UUID, transactionItem.getUuid());
                    cv.put(Namespace.FIELD_MEMBER_UUID, transactionItem.getMemberUuid());
                    cv.put(Namespace.FIELD_DEBIT, transactionItem.getDebit());
                    cv.put(Namespace.FIELD_CREDIT, transactionItem.getCredit());
                    cv.put(Namespace.FIELD_TRANSACTION_UUID, draftTransaction.getUuid());

                    db.insert(Namespace.TABLE_TRANSACTION_ITEMS, null, cv);

                };

                draftTransaction.getCreditItems().ForEach(transactionItemForEach);
                draftTransaction.getDebitItems().Filter(t -> t.getDebit() > 0).ForEach(transactionItemForEach);

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }


        }

        return getTransactionByUuid(draftTransaction.getUuid());


    }

    public Transaction getTransactionByUuid(String transactionUuid) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRANSACTION +
                " WHERE " + Namespace.FIELD_UUID + " = '" + transactionUuid + "';";

        String sqlTransactionItems = "SELECT * FROM " + Namespace.TABLE_TRANSACTION_ITEMS +
                " WHERE " + Namespace.FIELD_TRANSACTION_UUID + " = '" + transactionUuid + "';";

        LocalTransaction result = new QueryResult<>(sql, LocalTransaction.class).getFirstRow();

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {

            try (Cursor sqlResult = db.rawQuery(sqlTransactionItems, null)) {
                if (sqlResult.moveToFirst()) {
                    do {
                        LocalTransactionItem localTransactionItem = new LocalTransactionItem(sqlResult);
                        if (localTransactionItem.getCredit() > 0) {
                            result.addCreditItem(localTransactionItem);
                        }

                        if (localTransactionItem.getDebit() > 0) {
                            result.addDebitItem(localTransactionItem);
                        }
                    }
                    while (sqlResult.moveToNext());
                }
            }

        }

        return result;
    }

    public List<Transaction> getTransactionsByTrip(String tripUuid) {

        String sql = "SELECT * FROM " + Namespace.TABLE_TRANSACTION + " WHERE " + Namespace.FIELD_TRIP_UUID + " = '" + tripUuid + "'";

        String sqlTransactionItems = "SELECT " +
                "  i." + Namespace.FIELD_ID +
                ", i." + Namespace.FIELD_MEMBER_UUID +
                ", i." + Namespace.FIELD_CREDIT +
                ", i." + Namespace.FIELD_DEBIT +
                ", i." + Namespace.FIELD_TRANSACTION_UUID +

                " FROM " + Namespace.TABLE_TRANSACTION_ITEMS + " as i" +
                " INNER JOIN " + Namespace.TABLE_TRANSACTION + " as t ON t." + Namespace.FIELD_UUID + " = i." + Namespace.FIELD_TRANSACTION_UUID +
                " WHERE t." + Namespace.FIELD_TRIP_UUID + " = '" + tripUuid + "';";


        Map<String, LocalTransaction> localTransactionLongSparseArray = new HashMap<>();

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {

            //Сначала считываем все транзакции
            try (Cursor sqlResult = db.rawQuery(sql, null)) {
                if (sqlResult.moveToFirst()) {
                    do {
                        LocalTransaction localTransaction = new LocalTransaction(sqlResult);
                        localTransactionLongSparseArray.put(localTransaction.getUuid(), localTransaction);
                    }
                    while (sqlResult.moveToNext());
                }
            }
        }

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {
            //после того. как у нас появилась мапа транзакций добавляем в нее проводки
            try (Cursor sqlResult = db.rawQuery(sqlTransactionItems, null)) {
                if (sqlResult.moveToFirst()) {
                    do {
                        LocalTransactionItem localTransactionItem = new LocalTransactionItem(sqlResult);

                        LocalTransaction transaction = localTransactionLongSparseArray.get(localTransactionItem.getTransactionUuid());
                        if (transaction != null) {

                            if (localTransactionItem.getCredit() > 0) {
                                transaction.addCreditItem(localTransactionItem);
                            }

                            if (localTransactionItem.getDebit() > 0) {
                                transaction.addDebitItem(localTransactionItem);
                            }

                        }
                    }
                    while (sqlResult.moveToNext());
                }
            }
        }


        List<Transaction> result = new ArrayList<>(localTransactionLongSparseArray.size());
        result.addAll(localTransactionLongSparseArray.values());

        Collections.sort(result, (left, right) -> right.getDate().compareTo(left.getDate()));

        return result;

    }


    public void setActive(String uuid, boolean active) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, active ? 1 : 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_TRANSACTION, cv, Namespace.FIELD_UUID + " = '" + uuid + "'", null);
        }
    }
}
