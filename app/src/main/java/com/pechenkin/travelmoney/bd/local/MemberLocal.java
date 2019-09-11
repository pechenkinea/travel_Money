package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;
import android.graphics.Color;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;


/**
 * Created by pechenkin on 04.04.2018.
 * Участник
 */

public class MemberLocal extends IdAndNameTableRow implements Member {


    private int color;
    private int icon;
    private String uuid;
    private boolean active = true;
    //private String tripUuid;

    public MemberLocal(Cursor c) {
        super(c);

        int col = getIntColumnValue(Namespace.FIELD_COLOR, c);
        if (col == -1 || col == 0) {
            col = Color.BLACK;
        }
        this.color = col;

        this.icon = getIntColumnValue(Namespace.FIELD_ICON, c);
        this.uuid = getStringColumnValue(Namespace.FIELD_UUID, c);
        this.active = getIntColumnValue(Namespace.FIELD_ACTIVE, c) == 1;
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
    public String getUuid() {
        return this.uuid;
    }


    @Override
    public void edit(String name, int color, int icon) {

        if (!name.equals(this.name) || color != this.color || icon != this.icon) {

            TableMembers.INSTANCE.edit(this.id, name, color, icon);
            this.color = color;
            this.icon = icon;
        }

    }

    @Override
    public void setActive(boolean active) {
        TableMembers.INSTANCE.setActive(this.uuid, active);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Member) {
            return getId() == ((Member) obj).getId();
        }
        return false;
    }
}
