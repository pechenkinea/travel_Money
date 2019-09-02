package com.pechenkin.travelmoney.bd.local;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.bd.local.table.TransactionTable;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by pechenkin on 04.04.2018.
 * поездка, все данные по которой хранятся в локальной БД
 */

public class TripLocal implements Trip {

    //public final long processed;
    private final String comment;
    private final long id;
    private final String name;
    private final String uuid;

    public TripLocal(TripTableRow tripTableRow) {

        this.id = tripTableRow.id;
        this.comment = tripTableRow.comment;
        this.name = tripTableRow.name;
        this.uuid = tripTableRow.uuid;
    }


    @Override
    public String getUUID() {
        return this.uuid;
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
        TableTrip.INSTANCE.edit(this.id, name, comment);
    }

    @Override
    public StreamList<Member> getAllMembers() {
        return new StreamList<>(
                new ArrayList<>(
                        Arrays.asList(
                                TableMembers.INSTANCE.getAll().getAllRows()
                        )));
    }

    @Override
    public StreamList<Member> getActiveMembers() {

        return new StreamList<>(
                new ArrayList<>(
                        Arrays.asList(
                                TableMembers.INSTANCE.getAllByTripId(this.id)
                        )));

    }

    @Override
    public boolean memberIsActive(Member member) {
        return TableTrip.INSTANCE.isMemberInTrip(this.id, member.getId());
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        if (active) {
            TableTrip.INSTANCE.addMemberInTrip(this.id, member.getId());
        } else {
            TableTrip.INSTANCE.removeMemberInTrip(this.id, member.getId());
        }
    }


    @Override
    public StreamList<Transaction> getTransactions() {
        return new StreamList<>(
                TransactionTable.INSTANCE.getTransactionsByTrip(this.id)
        );
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        return TransactionTable.INSTANCE.addTransaction(this.id, draftTransaction);
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
    public Member getMemberById(long id) {
        return TableMembers.INSTANCE.getMemberById(id);
    }

    @Override
    public Date getStartDate() {
        return TableTrip.INSTANCE.getStartTripDate(this.id);
    }

    @Override
    public Date getEndDate() {
        return TableTrip.INSTANCE.getEndTripDate(this.id);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Trip) {
            return getUUID().equals(((Trip) obj).getUUID());
        }
        return false;
    }
}
