package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;
import java.util.Date;

/**
 * Декоратор для поездки. ничего не далет. просто вызвает методы из вложенного Trip
 * Нужно для того, что бы в классе для кэширования не переопределять все методы. а только те, которые работают с кэшем
 */
public class TripDecorator implements Trip {

    private final Trip trip;

    TripDecorator(Trip trip) {
        this.trip = trip;
    }

    @Override
    public String getUUID() {
        return this.trip.getUUID();
    }

    @Override
    public String getName() {
        return this.trip.getName();
    }

    @Override
    public String getComment() {
        return this.trip.getComment();
    }

    @Override
    public void edit(String name, String comment) {
        this.trip.edit(name, comment);
    }


    @Override
    public Date getStartDate() {
        return this.trip.getStartDate();
    }

    @Override
    public Date getEndDate() {
        return this.trip.getEndDate();
    }

    @Override
    public StreamList<Member> getAllMembers() {
        return this.trip.getAllMembers();
    }

    @Override
    public StreamList<Member> getActiveMembers() {
        return this.trip.getActiveMembers();
    }

    @Override
    public Member createMember(String name, int color, int icon) {
        return this.trip.createMember(name, color, icon);
    }

    @Override
    public Member getMe() {
        return this.trip.getMe();
    }

    @Override
    public Member getMemberByName(String name) {
        return this.trip.getMemberByName(name);
    }

    @Override
    public Member getMemberByUuid(String uuid) {
        return this.trip.getMemberByUuid(uuid);
    }

    @Override
    public boolean memberIsActive(Member member) {
        return this.trip.memberIsActive(member);
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        this.trip.setMemberActive(member, active);
    }

    @Override
    public StreamList<Transaction> getTransactions() {
        return this.trip.getTransactions();
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        return this.trip.addTransaction(draftTransaction);
    }


    @SuppressWarnings("all")
    @Override
    public boolean equals(Object obj) {
        return this.trip.equals(obj);
    }
}
