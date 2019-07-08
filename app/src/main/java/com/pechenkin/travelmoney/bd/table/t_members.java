package com.pechenkin.travelmoney.bd.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.LongSparseArray;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.NamesHashMap;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.result.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.result.QueryResultFactory;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;

import java.util.Map;

public class t_members {

    static public Boolean isAdded(String user_name) {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_NAME + " = '" + user_name + "'";
        BaseQueryResult result = QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);
        return result != null && result.hasRows();
    }

    static public long add(String name, int color) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);
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
        return rowID;
    }

    static public void edit(long id, String name, int color) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_NAME, name);

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

    static public MembersQueryResult getAll() {
        String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS;
        return QueryResultFactory.createQueryResult(sql, MembersQueryResult.class);

    }

    static private NamesHashMap<BaseTableRow> membersNamesCache = new NamesHashMap<>();

    static public void updateMembersCache() {
        membersNamesCache.clear();

        MembersQueryResult allMembers = getAllByTripId(t_trips.ActiveTrip.id);
        if (allMembers.hasRows()) {
            for (MemberBaseTableRow member : allMembers.getAllRows()) {
                membersNamesCache.put(member.name, member);
            }
        }
    }

    static public long getIdByName(String m_name) {
        BaseTableRow row = membersNamesCache.get(m_name);
        if (row == null) {
            String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_NAME + " = '" + m_name + "'";
            BaseQueryResult result = QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);
            if (result != null) {
                row = result.getFirstRow();
                if (row != null) {
                    membersNamesCache.put(m_name, row);
                    return row.id;
                }
            }
            return -1;
        } else
            return row.id;
    }


    static public long getIdByNameCache(String m_name) {
        BaseTableRow row = membersNamesCache.get(m_name);
        if (row == null) {
            return -1;
        } else
            return row.id;
    }

    static public long getMe() {
        return 1;
    }

    /**
     * Ищет участника с учетом падежей.
     * Для этого пробует найти участника по переданному имени. Если не удалось найти убирает последнюю букву от переданного значения и ищет по совпадению парвых символов.
     * Так продолжается до тех пор пока не уменьшим переданное значение на 30% или оно не станет меньше 2х букв
     *
     * @param m_name строка для поиска
     * @return id сотудника или -1
     */
    static public long getIdByNameCase(String m_name) {

        String nameCase = NamesHashMap.keyValidate(m_name);
        BaseTableRow row = membersNamesCache.get(nameCase);

        while (row == null && nameCase.length() > 2 && nameCase.length() / m_name.length() > 0.7) {
            nameCase = nameCase.substring(0, nameCase.length() - 1);

            for (Map.Entry<String, BaseTableRow> entry : membersNamesCache.entrySet()) {
                String rowCache = entry.getKey();
                if (rowCache.startsWith(nameCase)) {
                    row = entry.getValue();
                    break;
                }
            }
        }

        if (row != null)
            return row.id;
        else
            return -1;

    }


    static private LongSparseArray<MemberBaseTableRow> memberCache = new LongSparseArray<>();


    static public MemberBaseTableRow getMemberById(long _id) {
        MemberBaseTableRow result = memberCache.get(_id);
        if (result == null) {
            String sql = "SELECT * FROM " + Namespace.TABLE_MEMBERS + " WHERE " + Namespace.FIELD_ID + " = '" + _id + "'";
            MembersQueryResult find = QueryResultFactory.createQueryResult(sql, MembersQueryResult.class);
            if (find != null) {
                result = find.getFirstRow();
                memberCache.put(_id, result);
            }
        }
        return result;
    }

    static public MembersQueryResult getAllByTripId(long t_id) {
        String sql = "SELECT m." + Namespace.FIELD_ID + ", m." + Namespace.FIELD_NAME + ", m." + Namespace.FIELD_COLOR
                + " FROM " + Namespace.TABLE_TRIPS_MEMBERS + " as t"
                + " inner join " + Namespace.TABLE_MEMBERS + " as m"
                + " on t." + Namespace.FIELD_MEMBER + " = m." + Namespace.FIELD_ID + " and t." + Namespace.FIELD_TRIP + " = '" + t_id + "'";

        return QueryResultFactory.createQueryResult(sql, MembersQueryResult.class);
    }

}
