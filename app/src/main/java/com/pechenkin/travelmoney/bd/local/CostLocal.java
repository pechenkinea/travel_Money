package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_costs;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.Date;


/**
 * Created by pechenkin on 04.04.2018.
 * Трата
 */

public class CostLocal extends IdTableRow implements Cost {

    private final String comment;
    private final String image_dir;

    private final Date date;

    private final Member member;
    private final Member to_member;
    //public final int currency;
    private long active;
    private long repayment;
    //public final int trip;

    private final double sum;


    public CostLocal(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.sum = getDoubleColumnValue(Namespace.FIELD_SUM, c);
        this.image_dir = getStringColumnValue(Namespace.FIELD_IMAGE_DIR, c);
        this.date = getDateColumnValue(Namespace.FIELD_DATE, c);

        this.member = t_trips.getActiveTrip().getMemberById(getLongColumnValue(Namespace.FIELD_MEMBER, c));
        this.to_member = t_trips.getActiveTrip().getMemberById(getLongColumnValue(Namespace.FIELD_TO_MEMBER, c));
        //currency = getLongColumnValue(Namespace.FIELD_CURRENCY, c);
        this.active = getLongColumnValue(Namespace.FIELD_ACTIVE, c);
        this.repayment = getLongColumnValue(Namespace.FIELD_REPAYMENT, c);
        //trip = getLongColumnValue(Namespace.FIELD_TRIP, c);
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public Member getToMember() {
        return to_member;
    }

    @Override
    public double getSum() {
        return sum;
    }


    @Override
    public boolean isActive() {
        return active != 0;
    }

    @Override
    public boolean isRepayment() {
        return repayment != 0;
    }

    @Override
    public void setActive(boolean value) {
        t_costs.setCostState(this.id, value);
        active = value ? 1 : 0;
    }

    @Override
    public String getImageDir() {
        return image_dir;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getComment() {
        return comment;
    }

}