package com.pechenkin.travelmoney.bd;


import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Кеширование значений методов. Нужно, что бы лишний раз не лазить в базу (для удаленной БД особенно актуально).
 */ //TODO доделать кэширование  и оборачивать поездку при создании в это декоратор
public class TripCacheDecorator extends TripDecorator {

    public TripCacheDecorator(Trip trip) {
        super(trip);
    }

    private List<Member> allMembers = null;
    private List<Member> activeMembers = null;
    private List<Transaction> cacheTransactions = null;

    @Override
    public List<Member> getAllMembers() {
        if (allMembers == null) {
            allMembers = super.getAllMembers();
        }
        return allMembers;
    }

    @Override
    public List<Member> getActiveMembers() {
        if (activeMembers == null) {
            activeMembers = super.getActiveMembers();
        }
        return activeMembers;
    }

    @Override
    public Member createMember(String name, int color, int icon) {

        getAllMembers(); //Если до этого ни разу не вызывали

        Member createdMember = super.createMember(name, color, icon);
        allMembers.add(createdMember);
        return createdMember;
    }

    @Override
    public boolean memberIsActive(Member member) {
        return super.memberIsActive(member);
    }

    @Override
    public void setMemberActive(Member member, boolean active) {

        super.setMemberActive(member, active);

        for (int i = 0; i < getActiveMembers().size(); i++) {
            Member existMember = getActiveMembers().get(i);
            if (existMember.equals(member)) {
                if (!active) {
                    activeMembers.remove(i);
                }

                return;
            }
        }

        if (active) {
            activeMembers.add(member);
        }

    }


    @Override
    public List<Transaction> getTransactions() {
        if (cacheTransactions == null) {
            cacheTransactions = super.getTransactions();
        }
        return cacheTransactions;
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        Transaction transaction = super.addTransaction(draftTransaction);
        cacheTransactions.add(transaction);
        return transaction;
    }



}
