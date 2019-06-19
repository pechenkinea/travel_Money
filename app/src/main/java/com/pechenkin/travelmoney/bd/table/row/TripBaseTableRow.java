package com.pechenkin.travelmoney.bd.table.row;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Namespace;

/**
 * Created by pechenkin on 04.04.2018.
 * поездка
 */

public class TripBaseTableRow extends BaseTableRow {

    //public final long processed;
    public final String comment;

    public TripBaseTableRow(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        //processed = getLongColumnValue(Namespace.FIELD_PROCESSED, c);
    }

    private TripBaseTableRow() {
        super(null);
        this.comment = "";
    }

    public static TripBaseTableRow getEmpty()
    {
        return  new TripBaseTableRow();
    }
}
