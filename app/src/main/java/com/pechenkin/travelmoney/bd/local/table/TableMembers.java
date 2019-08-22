package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.LongSparseArray;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.bd.local.MemberLocal;

public class TableMembers {

    public static TableMembers INSTANCE = new TableMembers();

    private TableMembers() {

    }

    public Member add(String name, int color, int icon) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
        cv.put(Namespace.FIELD_ICON, icon);

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

        memberCache.remove(id);
    }

    public QueryResult<MemberLocal> getAll() {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS;
        return new QueryResult<>(sql, MemberLocal.class);

    }


    public Member getMemberByName(String name) {

        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_NAME + " = '" + name + "'";
        QueryResult<MemberLocal> result = new QueryResult<>(sql, MemberLocal.class);
        return result.getFirstRow();

    }


    private final LongSparseArray<Member> memberCache = new LongSparseArray<>();


    public Member getMemberById(long _id) {
        Member result = memberCache.get(_id);
        if (result == null) {
            String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_ID + " = '" + _id + "'";
            QueryResult<MemberLocal> find = new QueryResult<>(sql, MemberLocal.class);
            result = find.getFirstRow();
            memberCache.put(_id, result);
        }
        return result;
    }

    public Member[] getAllByTripId(long t_id) {
        String sql = "SELECT m." + Namespace.FIELD_ID + ", m." + Namespace.FIELD_NAME + ", m." + Namespace.FIELD_COLOR + ", m." + Namespace.FIELD_ICON
                + " FROM " + Namespace.TABLE_TRIPS_MEMBERS + " as t"
                + " inner join " + Namespace.TABLE_MEMBERS + " as m"
                + " on t." + Namespace.FIELD_MEMBER + " = m." + Namespace.FIELD_ID + " and t." + Namespace.FIELD_TRIP + " = '" + t_id + "'";

        return new QueryResult<>(sql, MemberLocal.class).getAllRows();
    }

}
