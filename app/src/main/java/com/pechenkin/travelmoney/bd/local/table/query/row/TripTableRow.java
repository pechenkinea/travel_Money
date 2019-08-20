package com.pechenkin.travelmoney.bd.local.table.query.row;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.Namespace;
import com.pechenkin.travelmoney.bd.local.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_costs;
import com.pechenkin.travelmoney.bd.local.table.t_members;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.Date;

/**
 * Created by pechenkin on 04.04.2018.
 * поездка
 */

public class TripTableRow extends IdAndNameTableRow implements Trip {

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

    public static TripTableRow getEmpty() {
        return new TripTableRow();
    }


    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void edit(String name, String comment) {
        t_trips.edit(this.id, name, comment);
    }

    @Override
    public Member[] getAllMembers() {
        return t_members.getAll().getAllRows();
    }

    @Override
    public Member[] getActiveMembers() {
        return t_members.getAllByTripId(this.id).getAllRows();
    }

    @Override
    public boolean memberIsActive(Member member) {
        return t_trips.isMemberInTrip(this.id, member.getId());
    }

    @Override
    public void addMember(Member member) {
        t_trips.addMemberInTrip(this.id, member.getId());
    }

    @Override
    public void removeMember(Member member) {
        t_trips.removeMemberInTrip(this.id, member.getId());
    }

    @Override
    public void addCost(long member_id, long to_member_id, String comment, double sum, String image_dir, Date date, boolean isRepayment) {
        t_costs.add(member_id, to_member_id, comment, sum, image_dir, this.id, date, isRepayment);
    }

    @Override
    public boolean isActive() {
        return t_trips.isActive(this.id);
    }

    @Override
    public Cost[] getAllCost() {
        return t_costs.getAllByTripId(this.id).getAllRows();
    }
}
