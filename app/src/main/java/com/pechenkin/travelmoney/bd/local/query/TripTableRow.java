package com.pechenkin.travelmoney.bd.local.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.TripStore;
import com.pechenkin.travelmoney.bd.local.table.Namespace;

public class TripTableRow extends IdAndNameTableRow{

    public final String comment;
    public final String uuid;
    public final TripStore tripStore;

    public TripTableRow(Cursor c) {
        super(c);
        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.uuid = getStringColumnValue(Namespace.FIELD_UUID, c);

        tripStore = TripStore.valueOf(getStringColumnValue(Namespace.FIELD_STORE, c));

    }
}
