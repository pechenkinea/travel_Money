package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.t_costs;
import com.pechenkin.travelmoney.bd.local.table.t_members;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pechenkin on 04.04.2018.
 * поездка
 */

public class TripLocal extends IdAndNameTableRow implements Trip {

    //public final long processed;
    public final String comment;

    public TripLocal(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        //processed = getLongColumnValue(Namespace.FIELD_PROCESSED, c);
    }

    private TripLocal() {
        super(null);
        this.comment = "";
    }

    public static TripLocal getEmpty() {
        return new TripLocal();
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
    public List<Member> getAllMembers() {
        return new ArrayList<>(
                Arrays.asList(
                        t_members.getAll().getAllRows()
                ));
    }

    @Override
    public List<Member> getActiveMembers() {

        return new ArrayList<>(
                Arrays.asList(
                        t_members.getAllByTripId(this.id)
                ));

    }

    @Override
    public boolean memberIsActive(Member member) {
        return t_trips.isMemberInTrip(this.id, member.getId());
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        if (active) {
            t_trips.addMemberInTrip(this.id, member.getId());
        } else {
            t_trips.removeMemberInTrip(this.id, member.getId());
        }
    }

    @Override
    public void addCost(Member member, Member toMember, String comment, double sum, String image_dir, Date date, boolean isRepayment) {
        t_costs.add(member.getId(), toMember.getId(), comment, sum, image_dir, this.id, date, isRepayment);
    }

    @Override
    public boolean isActive() {
        return t_trips.isActive(this.id);
    }

    @Override
    public Cost[] getAllCost() {
        return t_costs.getAllByTripId(this.id).getAllRows();
    }

    @Override
    public Member createMember(String name, int color, int icon) {
        return t_members.add(name, color, icon);
    }

    @Override
    public Member getMe() {
        return getMemberById(1);
    }

    @Override
    public Member getMemberById(long id) {
        return t_members.getMemberById(id);
    }

    @Override
    public Date getStartDate() {
        return t_trips.getStartTripDate(this.id);
    }

    @Override
    public Date getEndDate() {
        return t_trips.getEndTripDate(this.id);
    }
}
