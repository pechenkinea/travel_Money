package com.pechenkin.travelmoney.bd.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.query.row.CostTableRow;

import java.util.Date;

public class t_costs {

    private t_costs() {
    }

    static public void add(long member_id, long to_member_id, String comment, double sum, String image_dir, long tripId, Date date) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_MEMBER, member_id);
        cv.put(Namespace.FIELD_TO_MEMBER, to_member_id);
        cv.put(Namespace.FIELD_COMMENT, comment);
        cv.put(Namespace.FIELD_SUM, sum);
        cv.put(Namespace.FIELD_IMAGE_DIR, image_dir);
        cv.put(Namespace.FIELD_ACTIVE, 1);
        cv.put(Namespace.FIELD_TRIP, tripId);
        cv.put(Namespace.FIELD_DATE, date.getTime());

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.insert(Namespace.TABLE_COSTS, null, cv);
        }
    }

    static public BaseQueryResult<CostTableRow> getAllByTripId(long t_id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_COSTS + " where " + Namespace.FIELD_TRIP + " = '" + t_id + "' ORDER BY " + Namespace.FIELD_DATE + " DESC";
        return new BaseQueryResult<>(sql, CostTableRow.class);
    }

    static public void disable_cost(long id) {

        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_COSTS, cv, Namespace.FIELD_ID + " = " + id, null);
        }

    }

    static public void enable_cost(long id) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, 1);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_COSTS, cv, Namespace.FIELD_ID + " = " + id, null);
        }

    }


}
