package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.LongSparseArray;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.NamesHashMap;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.bd.local.MemberLocal;

import java.util.List;

public class t_members {

    public static Member add(String name, int color, int icon) {
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

        updateMembersCache();
        return getMemberById(rowID);
    }

    static public void edit(long id, String name, int color, int icon) {
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
        updateMembersCache();
    }

    static public QueryResult<MemberLocal> getAll() {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS;
        return new QueryResult<>(sql, MemberLocal.class);

    }

    static private final NamesHashMap<Member> membersNamesCache = new NamesHashMap<>();

    static public void updateMembersCache() {
        membersNamesCache.clear();

        List<Member> allMembers = t_trips.getActiveTrip().getAllMembers();
        for (Member member : allMembers){
            membersNamesCache.put(member.getName(), member);
        }
    }

    static public long getIdByName(String m_name) {
        Member row = membersNamesCache.get(m_name);
        if (row == null) {
            String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_NAME + " = '" + m_name + "'";
            QueryResult<MemberLocal> result = new QueryResult<>(sql, MemberLocal.class);
            row = result.getFirstRow();
            if (row != null) {
                membersNamesCache.put(m_name, row);
                return row.getId();
            }
            return -1;
        } else
            return row.getId();
    }


    static public Member getIdByNameCache(String m_name) {
        return membersNamesCache.get(m_name);
    }

    static private final LongSparseArray<Member> memberCache = new LongSparseArray<>();


    public static Member getMemberById(long _id) {
        Member result = memberCache.get(_id);
        if (result == null) {
            String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_ID + " = '" + _id + "'";
            QueryResult<MemberLocal> find = new QueryResult<>(sql, MemberLocal.class);
            result = find.getFirstRow();
            memberCache.put(_id, result);
        }
        return result;
    }

    static public Member[] getAllByTripId(long t_id) {
        String sql = "SELECT m." + Namespace.FIELD_ID + ", m." + Namespace.FIELD_NAME + ", m." + Namespace.FIELD_COLOR + ", m." + Namespace.FIELD_ICON
                + " FROM " + Namespace.TABLE_TRIPS_MEMBERS + " as t"
                + " inner join " + Namespace.TABLE_MEMBERS + " as m"
                + " on t." + Namespace.FIELD_MEMBER + " = m." + Namespace.FIELD_ID + " and t." + Namespace.FIELD_TRIP + " = '" + t_id + "'";

        return new QueryResult<>(sql, MemberLocal.class).getAllRows();
    }

}
