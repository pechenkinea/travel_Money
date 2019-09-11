package com.pechenkin.travelmoney.bd.local;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.bd.local.table.TableTransaction;
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


    public long getId() {
        return id;
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
                                TableMembers.INSTANCE.getAllWithOutTrip()
                        )));
    }

    @Override
    public StreamList<Member> getActiveMembers() {

        return new StreamList<>(
                new ArrayList<>(
                        Arrays.asList(
                                TableMembers.INSTANCE.getAllByTripUuid(this.uuid)
                        )));

    }

    @Override
    public boolean memberIsActive(Member member) {
        return TableTrip.INSTANCE.isMemberInTrip(this.uuid, member.getUuid());
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        if (active) {
            TableTrip.INSTANCE.addMemberInTrip(this.uuid, member.getUuid());
        } else {
            TableTrip.INSTANCE.removeMemberInTrip(this.uuid, member.getUuid());
        }
    }


    @Override
    public StreamList<Transaction> getTransactions() {
        return new StreamList<>(
                TableTransaction.INSTANCE.getTransactionsByTrip(this.uuid)
        );
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        return TableTransaction.INSTANCE.addTransaction(this.uuid, draftTransaction);
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
        return TableMembers.INSTANCE.getMemberByNameWithOutTrip(name);
    }

    @Override
    public Member getMemberByUuid(String uuid) {
        return TableMembers.INSTANCE.getMemberByUuid(uuid);
    }


    @Override
    public Date getStartDate() {
        return TableTrip.INSTANCE.getStartTripDate(this.uuid);
    }

    @Override
    public Date getEndDate() {
        return TableTrip.INSTANCE.getEndTripDate(this.uuid);
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
