package com.pechenkin.travelmoney.bd.table.row;

import android.database.Cursor;
import android.graphics.Color;

import com.pechenkin.travelmoney.bd.Namespace;


/**
 * Created by pechenkin on 04.04.2018.
 * Участник
 */

public class MemberBaseTableRow extends BaseTableRow {


    public final int color;
    public final int icon;

    public MemberBaseTableRow(Cursor c) {
        super(c);

        int col = getIntColumnValue(Namespace.FIELD_COLOR, c);
        if (col == -1 || col == 0)
        {
            col = Color.BLACK;
        }
        this.color = col;

        this.icon  = getIntColumnValue(Namespace.FIELD_ICON, c);
    }
}
