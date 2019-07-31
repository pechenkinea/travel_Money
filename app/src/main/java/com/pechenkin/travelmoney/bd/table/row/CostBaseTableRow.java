package com.pechenkin.travelmoney.bd.table.row;

import android.database.Cursor;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.cost.Cost;
import java.util.Date;


/**
 * Created by pechenkin on 04.04.2018.
 * Трата
 */

public class CostBaseTableRow extends BaseTableRow implements Cost {

    private final String comment;
    private final String image_dir;

    private final Date date;

    private final long member;
    private final long to_member;
    //public final int currency;
    private long  active;
    //public final int trip;
    private  int groupId = 0;

    private final double sum;


    public CostBaseTableRow(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.sum = getDoubleColumnValue(Namespace.FIELD_SUM, c);
        this.image_dir = getStringColumnValue(Namespace.FIELD_IMAGE_DIR, c);
        this.date = getDateColumnValue(Namespace.FIELD_DATE, c);

        this.member = getLongColumnValue(Namespace.FIELD_MEMBER, c);
        this.to_member = getLongColumnValue(Namespace.FIELD_TO_MEMBER, c);
        //currency = getLongColumnValue(Namespace.FIELD_CURRENCY, c);
        this.active = getLongColumnValue(Namespace.FIELD_ACTIVE, c);
        //trip = getLongColumnValue(Namespace.FIELD_TRIP, c);
    }


    @Override
    public long id() {
        return id;
    }

    @Override
    public long member() {
        return member;
    }

    @Override
    public long to_member() {
        return to_member;
    }

    @Override
    public double sum() {
        return sum;
    }


    @Override
    public long active() {
        return active;
    }

    @Override
    public void setActive(int i) {
        active = i;
    }

    @Override
    public String image_dir() {
        return image_dir;
    }

    @Override
    public Date date() {
        return date;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public void setGroupId(int groupId){
        this.groupId = groupId;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }
}
