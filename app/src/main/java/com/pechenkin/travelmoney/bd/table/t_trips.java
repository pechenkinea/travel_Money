package com.pechenkin.travelmoney.bd.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.query.QueryResultFactory;
import com.pechenkin.travelmoney.bd.table.query.trip.TripsQueryResult;
import com.pechenkin.travelmoney.bd.table.query.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.query.trip.TripTableRow;

import java.util.Date;

public class t_trips {

    static public TripTableRow ActiveTrip;

    static {
        ActiveTrip = getActiveTrip();
    }

    static public long add(String name, String comment) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_COMMENT, comment);


        long rowID;

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            rowID = db.insert(Namespace.TABLE_TRIPS, null, cv);
        }
        return rowID;
    }

    static public void edit(long id, String name, String comment) {


        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_COMMENT, comment);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_ID + " = " + id, null);
        }
    }

    static public Boolean isAdded(String trip_name) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_NAME + " = '" + trip_name + "'";
        BaseQueryResult result = QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);
        return result != null && result.hasRows();
    }


    static public void removeMemberInTrip(long trip_id, long member_id) {
        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            db.delete(Namespace.TABLE_TRIPS_MEMBERS, Namespace.FIELD_TRIP + " = " + trip_id + " and " + Namespace.FIELD_MEMBER + " = " + member_id, null);
        }

    }

    static public void addMemberInTrip(long trip_id, long member_id) {
        removeMemberInTrip(trip_id, member_id);



        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_TRIP, trip_id);
        cv.put(Namespace.FIELD_MEMBER, member_id);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.insert(Namespace.TABLE_TRIPS_MEMBERS, null, cv);
        }
    }

    static public TripsQueryResult getAll() {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " ORDER BY " + Namespace.FIELD_ID + " DESC";
        return QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);
    }

    static public long getIdByName(String t_name) {
        String sql = "SELECT " + Namespace.FIELD_ID + " FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_NAME + " = '" + t_name + "'";

        BaseQueryResult result = QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);

        BaseTableRow row = result != null ? result.getFirstRow() : null;
        if (row != null)
            return row.id;

        return -1;
    }

    static public TripTableRow getTripById(long id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = " + id;

        TripsQueryResult result = QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);

        TripTableRow row = result != null ? result.getFirstRow() : null;
        if (row != null)
            return row;

        return null;
    }

    static public Boolean isMemberInTrip(long tripId, long memberId) {
        if (tripId < 0 || memberId < 0)
            return false;

        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS_MEMBERS + " WHERE " + Namespace.FIELD_TRIP + " = '" + tripId + "' and " + Namespace.FIELD_MEMBER + " = '" + memberId + "'";
        BaseQueryResult result = QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);

        return result != null && result.hasRows();

    }

    static public void set_active(long t_id) {
        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            ContentValues cv = new ContentValues();
            cv.put(Namespace.FIELD_PROCESSED, "0");
            db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_PROCESSED + " != 0", null);

            ContentValues cv2 = new ContentValues();
            cv2.put(Namespace.FIELD_PROCESSED, "1");

            db.update(Namespace.TABLE_TRIPS, cv2, Namespace.FIELD_ID + " = " + t_id, null);
        }

        ActiveTrip = getActiveTrip();
    }

    static public Boolean isActive(long t_id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = '" + t_id + "' and " + Namespace.FIELD_PROCESSED + " = 1";

        TripsQueryResult result = QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);
        return result != null && result.hasRows();
    }



    private static TripTableRow getActiveTrip() {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_PROCESSED + " = '1'";

        TripsQueryResult result = null;
        int tryCount = 0;
        boolean wait = false;

        while ((result == null || !result.hasRows()) && ++tryCount < 5) {
            if (wait) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return TripTableRow.getEmpty();
                }
            }
            wait = true;
            result = QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);
        }

        if (result == null || !result.hasRows())
            return TripTableRow.getEmpty();

        return result.getFirstRow();

    }

    public static Date getStartTripDate(long t_id){
        String query = String.format("SELECT MIN(%s) FROM %s WHERE %s = '%s'",
                Namespace.FIELD_DATE,
                Namespace.TABLE_COSTS,
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

    public static Date getEndTripDate(long t_id){
        String query = String.format("SELECT MAX(%s) FROM %s WHERE %s = '%s'",
                Namespace.FIELD_DATE,
                Namespace.TABLE_COSTS,
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
