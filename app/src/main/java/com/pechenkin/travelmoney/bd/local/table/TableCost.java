package com.pechenkin.travelmoney.bd.local.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.bd.local.CostLocal;

import java.util.Date;

public class TableCost {

    public static TableCost INSTANCE = new TableCost();

    private TableCost() {
    }

    public void add(long member_id, long to_member_id, String comment, int sum, String image_dir, long tripId, Date date, boolean isRepayment) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_MEMBER, member_id);
        cv.put(Namespace.FIELD_TO_MEMBER, to_member_id);
        cv.put(Namespace.FIELD_COMMENT, comment);
        cv.put(Namespace.FIELD_SUM, sum);
        cv.put(Namespace.FIELD_IMAGE_DIR, image_dir);
        cv.put(Namespace.FIELD_ACTIVE, 1);
        cv.put(Namespace.FIELD_TRIP, tripId);
        cv.put(Namespace.FIELD_DATE, date.getTime());
        cv.put(Namespace.FIELD_REPAYMENT, isRepayment ? 1 : 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.insert(Namespace.TABLE_COSTS, null, cv);
        }
    }

    public QueryResult<CostLocal> getAllByTripId(long t_id) {
        String sql = "SELECT * FROM " + Namespace.TABLE_COSTS + " where " + Namespace.FIELD_TRIP + " = '" + t_id + "' ORDER BY " + Namespace.FIELD_DATE + " DESC";
        return new QueryResult<>(sql, CostLocal.class);
    }

    public void setCostState(long id, boolean active) {
        ContentValues cv = new ContentValues();
        cv.put(Namespace.FIELD_ACTIVE, active ? 1 : 0);

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {
            db.update(Namespace.TABLE_COSTS, cv, Namespace.FIELD_ID + " = " + id, null);
        }
    }


}
