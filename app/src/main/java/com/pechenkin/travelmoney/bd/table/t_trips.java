package com.pechenkin.travelmoney.bd.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.query.IdTableRow;
import com.pechenkin.travelmoney.bd.table.query.TableRow;
import com.pechenkin.travelmoney.bd.table.query.row.TripTableRow;

import java.util.Date;

public class t_trips {

    static private TripTableRow activeTrip = null;

    public static TripTableRow getActiveTrip() {
        if (activeTrip == null) {

            String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_PROCESSED + " = '1'";

            BaseQueryResult<TripTableRow> result = new BaseQueryResult<>(sql, TripTableRow.class);

            if (!result.hasRows())
                return TripTableRow.getEmpty();

            activeTrip = result.getFirstRow();
        }

        return activeTrip;

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
        activeTrip = null;
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
        BaseQueryResult<TableRow> result = new BaseQueryResult<>(sql, TableRow.class);
        return result.hasRows();
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

    static public BaseQueryResult<TripTableRow> getAll() {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " ORDER BY " + Namespace.FIELD_ID + " DESC";
        return new BaseQueryResult<>(sql, TripTableRow.class);
    }

    static public long getIdByName(String t_name) {
        String sql = "SELECT " + Namespace.FIELD_ID + " FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_NAME + " = '" + t_name + "'";

        BaseQueryResult<IdTableRow> result = new BaseQueryResult<>(sql, IdTableRow.class);

        IdTableRow row = result.getFirstRow();
        if (row != null)
            return row.id;

        return -1;
    }

    static public TripTableRow getTripById(long id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = " + id;

        BaseQueryResult<TripTableRow> result = new BaseQueryResult<>(sql, TripTableRow.class);

        TripTableRow row = result.getFirstRow();
        if (row != null)
            return row;

        return null;
    }

    static public Boolean isMemberInTrip(long tripId, long memberId) {
        if (tripId < 0 || memberId < 0)
            return false;

        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS_MEMBERS + " WHERE " + Namespace.FIELD_TRIP + " = '" + tripId + "' and " + Namespace.FIELD_MEMBER + " = '" + memberId + "'";
        BaseQueryResult result = new BaseQueryResult<>(sql, TableRow.class);

        return result.hasRows();
    }



    static public Boolean isActive(long t_id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = '" + t_id + "' and " + Namespace.FIELD_PROCESSED + " = 1";

        BaseQueryResult result = new BaseQueryResult<>(sql, TableRow.class);
        return result.hasRows();
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
