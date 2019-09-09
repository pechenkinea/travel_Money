package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.TripStore;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.bd.local.query.TableRow;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;

import java.util.Date;
import java.util.UUID;

public class TableTrip {

    public static final TableTrip INSTANCE = new TableTrip();

    private TableTrip() {

    }


    public TripTableRow getActiveTrip() {

        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_PROCESSED + " = '1'";

        QueryResult<TripTableRow> result = new QueryResult<>(sql, TripTableRow.class);

        if (!result.hasRows()) {
            sql = "SELECT * FROM " + Namespace.TABLE_TRIPS;
            result = new QueryResult<>(sql, TripTableRow.class);
        }

        return result.getFirstRow();
    }

    public void set_active(String uuid) {
        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            ContentValues cv = new ContentValues();
            cv.put(Namespace.FIELD_PROCESSED, "0");
            db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_PROCESSED + " != 0", null);

            ContentValues cv2 = new ContentValues();
            cv2.put(Namespace.FIELD_PROCESSED, "1");

            db.update(Namespace.TABLE_TRIPS, cv2, Namespace.FIELD_UUID + " = '" + uuid + "'", null);
        }
    }


    public TripTableRow add(String name, String comment, TripStore tripStore, String uuid) {

        if (uuid.length() == 0){
            uuid = UUID.randomUUID().toString();
        }
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_COMMENT, comment);
        cv.put(Namespace.FIELD_UUID, uuid);
        cv.put(Namespace.FIELD_STORE, tripStore.toString());


        long rowID;

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            rowID = db.insert(Namespace.TABLE_TRIPS, null, cv);
        }
        return getById(rowID);
    }

    public void edit(long id, String name, String comment) {


        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_COMMENT, comment);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_ID + " = " + id, null);
        }
    }

    public void removeMemberInTrip(long trip_id, long member_id) {
        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            db.delete(Namespace.TABLE_TRIPS_MEMBERS, Namespace.FIELD_TRIP + " = " + trip_id + " and " + Namespace.FIELD_MEMBER + " = " + member_id, null);
        }

    }

    public void addMemberInTrip(long trip_id, long member_id) {
        removeMemberInTrip(trip_id, member_id);

        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_TRIP, trip_id);
        cv.put(Namespace.FIELD_MEMBER, member_id);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.insert(Namespace.TABLE_TRIPS_MEMBERS, null, cv);
        }
    }

    public TripTableRow[] getAll() {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " ORDER BY " + Namespace.FIELD_ID + " DESC";
        return new QueryResult<>(sql, TripTableRow.class).getAllRows();
    }

    public TripTableRow getById(long id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = '" + id + "'";

        QueryResult<TripTableRow> result = new QueryResult<>(sql, TripTableRow.class);

        return result.getFirstRow();
    }

    public Boolean isMemberInTrip(long tripId, long memberId) {
        if (tripId < 0 || memberId < 0)
            return false;

        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS_MEMBERS + " WHERE " + Namespace.FIELD_TRIP + " = '" + tripId + "' and " + Namespace.FIELD_MEMBER + " = '" + memberId + "'";
        QueryResult result = new QueryResult<>(sql, TableRow.class);

        return result.hasRows();
    }


    public Date getStartTripDate(long t_id) {
        String query = String.format("SELECT MIN(%s) FROM %s WHERE %s = '%s'",
                Namespace.FIELD_DATE,
                Namespace.TABLE_TRANSACTION,
                Namespace.FIELD_TRIP,
                t_id
        );

        Date result = null;

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {
            try (Cursor sqlResult = db.rawQuery(query, null)) {

                if (sqlResult.moveToFirst()) {
                    long value = sqlResult.getLong(0);
                    result = new Date(value);
                }
            }
        }
        return result;
    }

    public Date getEndTripDate(long t_id) {
        String query = String.format("SELECT MAX(%s) FROM %s WHERE %s = '%s'",
                Namespace.FIELD_DATE,
                Namespace.TABLE_TRANSACTION,
                Namespace.FIELD_TRIP,
                t_id
        );
        Date result = null;

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {
            try (Cursor sqlResult = db.rawQuery(query, null)) {

                if (sqlResult.moveToFirst()) {
                    long value = sqlResult.getLong(0);
                    result = new Date(value);
                }
            }
        }
        return result;
    }


}
