package com.pechenkin.travelmoney.bd.local;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.CostTable;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.TransactionTable;
import com.pechenkin.travelmoney.bd.local.table.TripsTable;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;

import java.security.InvalidParameterException;
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
        TripsTable.edit(this.id, name, comment);
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
        return TripsTable.isMemberInTrip(this.id, member.getId());
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        if (active) {
            TripsTable.addMemberInTrip(this.id, member.getId());
        } else {
            TripsTable.removeMemberInTrip(this.id, member.getId());
        }
    }



    @Override
    public boolean isActive() {
        return TripsTable.isActive(this.id);
    }


    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(
                Arrays.asList(
                        TransactionTable.INSTANCE.getTransactionsByTrip(this.id)
                ));
    }

    @Override
    public void addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        TransactionTable.addTransaction(this.id, draftTransaction);
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
        return TripsTable.getStartTripDate(this.id);
    }

    @Override
    public Date getEndDate() {
        return TripsTable.getEndTripDate(this.id);
    }
}
