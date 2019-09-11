package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.MemberLocal;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;

import java.util.UUID;

public class TableMembers {

    public static final TableMembers INSTANCE = new TableMembers();

    private TableMembers() {

    }

    public Member add(String name, int color, int icon) {
        return add(name, color, icon, "", UUID.randomUUID().toString());
    }

    public Member add(String name, int color, int icon, String tripUuid, String uuid) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_ICON, icon);
        cv.put(Namespace.FIELD_TRIP_UUID, tripUuid);
        cv.put(Namespace.FIELD_UUID, uuid);

        if (color != 0) {
            cv.put(Namespace.FIELD_COLOR, color);
        } else {
            cv.put(Namespace.FIELD_COLOR, Color.BLACK);
        }
        cv.put(Namespace.FIELD_ACTIVE, "1");
        long rowID;

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            rowID = db.insert(Namespace.TABLE_MEMBERS, null, cv);
        }

        return getMemberById(rowID);
    }

    public void edit(long id, String name, int color, int icon) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_ICON, icon);

        if (color != 0) {
            cv.put(Namespace.FIELD_COLOR, color);
        } else {
            cv.put(Namespace.FIELD_COLOR, Color.BLACK);
        }

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_MEMBERS, cv, Namespace.FIELD_ID + " = " + id, null);
        }
    }

    public void edit(String uuid, String name, int color, int icon) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_ICON, icon);
        cv.put(Namespace.FIELD_UUID, "");

        if (color != 0) {
            cv.put(Namespace.FIELD_COLOR, color);
        } else {
            cv.put(Namespace.FIELD_COLOR, Color.BLACK);
        }

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_MEMBERS, cv, Namespace.FIELD_UUID + " = " + uuid, null);
        }
    }

    public Member[] getAllWithOutTrip() {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_TRIP_UUID + " = '';";
        return new QueryResult<>(sql, MemberLocal.class).getAllRows();

    }


    public Member getMemberByNameWithOutTrip(String name) {

        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_NAME + " = '" + name + "' AND " + Namespace.FIELD_TRIP_UUID + " = '';";
        QueryResult<MemberLocal> result = new QueryResult<>(sql, MemberLocal.class);
        return result.getFirstRow();

    }
    public Member[] getMemberByTripUuid(String tripUuid) {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_TRIP_UUID + " = '" + tripUuid + "';";
        return new QueryResult<>(sql, MemberLocal.class).getAllRows();
    }


    public Member getMemberById(long _id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_ID + " = '" + _id + "'";
        QueryResult<MemberLocal> find = new QueryResult<>(sql, MemberLocal.class);
        return find.getFirstRow();
    }
    public Member getMemberByUuid(String uuid) {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_UUID + " = '" + uuid + "'";
        QueryResult<MemberLocal> find = new QueryResult<>(sql, MemberLocal.class);
        return find.getFirstRow();
    }

    public MemberLocal[] getAllByTripUuid(String uuid) {
        String sql = "SELECT " +
                "  m." + Namespace.FIELD_ID +
                ", m." + Namespace.FIELD_UUID +
                ", m." + Namespace.FIELD_NAME +
                ", m." + Namespace.FIELD_COLOR +
                ", m." + Namespace.FIELD_ICON +
                " FROM " + Namespace.TABLE_TRIPS_MEMBERS + " as t" +
                " inner join " + Namespace.TABLE_MEMBERS + " as m" +
                " on t." + Namespace.FIELD_MEMBER_UUID + " = m." + Namespace.FIELD_UUID + " and t." + Namespace.FIELD_TRIP_UUID + " = '" + uuid + "'";

        return new QueryResult<>(sql, MemberLocal.class).getAllRows();
    }


    public void setActive(String uuid, boolean active) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, active ? 1 : 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_MEMBERS, cv, Namespace.FIELD_UUID + " = " + uuid, null);
        }
    }

}
