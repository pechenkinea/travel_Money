package com.pechenkin.travelmoney.bd.local.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.table.Namespace;

public class TripTableRow extends IdAndNameTableRow{

    public final String comment;

    public TripTableRow(Cursor c) {
        super(c);
        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
    }
}
