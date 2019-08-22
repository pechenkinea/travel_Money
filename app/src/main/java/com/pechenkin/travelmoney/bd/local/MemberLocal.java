package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;
import android.graphics.Color;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;


/**
 * Created by pechenkin on 04.04.2018.
 * Участник
 */

public class MemberLocal extends IdAndNameTableRow implements Member {


    public final int color;
    public final int icon;

    public MemberLocal(Cursor c) {
        super(c);

        int col = getIntColumnValue(Namespace.FIELD_COLOR, c);
        if (col == -1 || col == 0) {
            col = Color.BLACK;
        }
        this.color = col;

        this.icon = getIntColumnValue(Namespace.FIELD_ICON, c);
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
        TableMembers.INSTANCE.edit(this.id, name, color, icon);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        return getId() == ((Member)obj).getId();
    }
}
