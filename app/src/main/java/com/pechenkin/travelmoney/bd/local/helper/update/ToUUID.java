package com.pechenkin.travelmoney.bd.local.helper.update;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.LongSparseArray;

import com.pechenkin.travelmoney.bd.local.LocalTransaction;
import com.pechenkin.travelmoney.bd.local.LocalTransactionItem;
import com.pechenkin.travelmoney.bd.local.MemberLocal;
import com.pechenkin.travelmoney.bd.local.helper.DBHelper;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;

import java.util.UUID;

public class ToUUID {

    public static void execute(SQLiteDatabase db){

        //участники
        LongSparseArray<String> memberMap = new LongSparseArray<>();
        try (Cursor sqlResult = db.rawQuery("SELECT * FROM " + Namespace.TABLE_MEMBERS, null)) {
            if (sqlResult.moveToFirst()) {
                do {
                    MemberLocal memberLocal = new MemberLocal(sqlResult);

                    if (memberLocal.getUuid().length() == 0) {

                        String uuid = UUID.randomUUID().toString();
                        ContentValues cv = new ContentValues();
                        cv.put(Namespace.FIELD_UUID, uuid);
                        db.update(Namespace.TABLE_MEMBERS, cv, Namespace.FIELD_ID + " = " + memberLocal.id, null);

                        memberMap.put(memberLocal.id, uuid);
                    }
                    else {
                        memberMap.put(memberLocal.id, memberLocal.getUuid());
                    }
                }
                while (sqlResult.moveToNext());
            }
        }

        //поездки
        LongSparseArray<String> tripMap = new LongSparseArray<>();
        try (Cursor sqlResult = db.rawQuery("SELECT * FROM " + Namespace.TABLE_TRIPS, null)) {
            if (sqlResult.moveToFirst()) {
                do {
                    TripTableRow tripLocal = new TripTableRow(sqlResult);
                    if (tripLocal.uuid.length() == 0){
                        String uuid = UUID.randomUUID().toString();
                        ContentValues cv = new ContentValues();
                        cv.put(Namespace.FIELD_UUID, uuid);
                        db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_ID + " = " + tripLocal.id, null);

                        tripMap.put(tripLocal.id, uuid);
                    }
                    else {
                        tripMap.put(tripLocal.id, tripLocal.uuid);
                    }
                }
                while (sqlResult.moveToNext());
            }
        }

        //Транзакции
        LongSparseArray<String> transactionMap = new LongSparseArray<>();
        try (Cursor sqlResult = db.rawQuery("SELECT * FROM " + Namespace.TABLE_TRANSACTION, null)) {
            if (sqlResult.moveToFirst()) {
                do {
                    LocalTransaction localTransaction = new LocalTransaction(sqlResult);

                    ContentValues cv = new ContentValues();

                    if (localTransaction.getUuid().length() == 0){
                        String uuid = UUID.randomUUID().toString();

                        cv.put(Namespace.FIELD_UUID, uuid);
                        transactionMap.put(localTransaction.id, uuid);
                    }
                    else {
                        transactionMap.put(localTransaction.id, localTransaction.getUuid());
                    }

                    long tripId = getLongFieldValue(sqlResult, Namespace.FIELD_TRIP);

                    cv.put(Namespace.FIELD_TRIP_UUID, tripMap.get(tripId));

                    db.update(Namespace.TABLE_TRANSACTION, cv, Namespace.FIELD_ID + " = " + localTransaction.id, null);
                }
                while (sqlResult.moveToNext());
            }
        }

        try (Cursor sqlResult = db.rawQuery("SELECT * FROM " + Namespace.TABLE_TRANSACTION_ITEMS, null)) {
            if (sqlResult.moveToFirst()) {
                do {
                    LocalTransactionItem localTransactionItem = new LocalTransactionItem(sqlResult);

                    ContentValues cv = new ContentValues();

                    if (localTransactionItem.getUuid().length() == 0) {
                        String uuid = UUID.randomUUID().toString();
                        cv.put(Namespace.FIELD_UUID, uuid);
                    }

                    long memberId = getLongFieldValue(sqlResult, Namespace.FIELD_MEMBER);
                    cv.put(Namespace.FIELD_MEMBER_UUID, memberMap.get(memberId));

                    long transactionId = getLongFieldValue(sqlResult, Namespace.FIELD_TRANSACTION);
                    cv.put(Namespace.FIELD_TRANSACTION_UUID, transactionMap.get(transactionId));

                    db.update(Namespace.TABLE_TRANSACTION_ITEMS, cv, Namespace.FIELD_ID + " = " + localTransactionItem.id, null);

                }
                while (sqlResult.moveToNext());
            }
        }



        db.execSQL("ALTER TABLE " + Namespace.TABLE_TRIPS_MEMBERS + " RENAME TO tmp_table_name");
        DBHelper.createTableTripsMembers(db);
        try (Cursor sqlResult = db.rawQuery("SELECT * FROM tmp_table_name", null)) {
            if (sqlResult.moveToFirst()) {
                do {

                    ContentValues cv = new ContentValues();

                    long tripId = getLongFieldValue(sqlResult, Namespace.FIELD_TRIP);
                    cv.put(Namespace.FIELD_TRIP_UUID, transactionMap.get(tripId));

                    long memberId = getLongFieldValue(sqlResult, Namespace.FIELD_MEMBER);
                    cv.put(Namespace.FIELD_MEMBER_UUID, memberMap.get(memberId));

                    db.insert(Namespace.TABLE_TRIPS_MEMBERS, null, cv);

                }
                while (sqlResult.moveToNext());
            }
        }
        db.execSQL("DROP TABLE tmp_table_name;");


    }

    private static long getLongFieldValue(Cursor sqlResult, String fieldName){
        int index = sqlResult.getColumnIndex(fieldName);
        return sqlResult.getLong(index);
    }
}
