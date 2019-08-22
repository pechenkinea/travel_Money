package com.pechenkin.travelmoney.bd.local;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableCost;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pechenkin on 04.04.2018.
 * поездка, все данные по которой хранятся в локальной БД
 */

public class TripLocal implements Trip {

    //public final long processed;
    private final String comment;
    private final long id;
    private final String name;

    public TripLocal(TripTableRow tripTableRow) {

        this.id = tripTableRow.id;
        this.comment = tripTableRow.comment;
        this.name = tripTableRow.name;
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
                        TableMembers.INSTANCE.getAll().getAllRows()
                ));
    }

    @Override
    public List<Member> getActiveMembers() {

        return new ArrayList<>(
                Arrays.asList(
                        TableMembers.INSTANCE.getAllByTripId(this.id)
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
        TableCost.INSTANCE.add(member.getId(), toMember.getId(), comment, sum, image_dir, this.id, date, isRepayment);
    }

    @Override
    public boolean isActive() {
        return t_trips.isActive(this.id);
    }

    @Override
    public List<Cost> getAllCost() {
        return new ArrayList<>(
                Arrays.asList(
                        TableCost.INSTANCE.getAllByTripId(this.id).getAllRows()
                ));

    }

    @Override
    public Member createMember(String name, int color, int icon) {
        return TableMembers.INSTANCE.add(name, color, icon);
    }

    @Override
    public Member getMe() {
        return TableMembers.INSTANCE.getMemberById(1);
    }



    @Override
    public Member getMemberByName(String name) {
        return TableMembers.INSTANCE.getMemberByName(name);
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
