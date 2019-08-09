package com.pechenkin.travelmoney.bd.table.query.row;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.IdAndNameTableRow;

/**
 * Created by pechenkin on 04.04.2018.
 * поездка
 */

public class TripTableRow extends IdAndNameTableRow {

    //public final long processed;
    public final String comment;

    public TripTableRow(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        //processed = getLongColumnValue(Namespace.FIELD_PROCESSED, c);
    }

    private TripTableRow() {
        super(null);
        this.comment = "";
    }

    public static TripTableRow getEmpty()
    {
        return  new TripTableRow();
    }
}
