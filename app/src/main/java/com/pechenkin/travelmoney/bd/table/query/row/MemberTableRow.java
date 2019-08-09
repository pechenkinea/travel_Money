package com.pechenkin.travelmoney.bd.table.query.row;

import android.database.Cursor;
import android.graphics.Color;

import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.table.t_trips;


/**
 * Created by pechenkin on 04.04.2018.
 * Участник
 */

public class MemberTableRow extends IdAndNameTableRow {


    public final int color;
    public final int icon;

    public MemberTableRow(Cursor c) {
        super(c);

        int col = getIntColumnValue(Namespace.FIELD_COLOR, c);
        if (col == -1 || col == 0)
        {
            col = Color.BLACK;
        }
        this.color = col;

        this.icon  = getIntColumnValue(Namespace.FIELD_ICON, c);
    }

    public boolean inTrip(long tripId){
        return t_trips.isMemberInTrip(tripId, this.id);
    }
}
