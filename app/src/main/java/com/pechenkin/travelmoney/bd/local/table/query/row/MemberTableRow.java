package com.pechenkin.travelmoney.bd.local.table.query.row;

import android.database.Cursor;
import android.graphics.Color;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.Namespace;
import com.pechenkin.travelmoney.bd.local.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_members;
import com.pechenkin.travelmoney.bd.local.table.t_trips;


/**
 * Created by pechenkin on 04.04.2018.
 * Участник
 */

public class MemberTableRow extends IdAndNameTableRow implements Member {


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

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public int getIcon() {
        return this.icon;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void edit(String name, int color, int icon) {
        t_members.edit(this.id, name, color, icon);

    }
}
