package com.pechenkin.travelmoney.bd;


import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * Кеширование значений методов. Нужно, что бы лишний раз не лазить в базу (для удаленной БД особенно актуально).
 */
public class TripCacheDecorator extends TripDecorator {

    public TripCacheDecorator(Trip trip) {
        super(trip);
    }

    private StreamList<Member> allMembers = null;
    private StreamList<Member> activeMembers = null;


    @Override
    public StreamList<Member> getAllMembers() {
        if (allMembers == null) {
            allMembers = super.getAllMembers();
        }
        return allMembers;
    }

    @Override
    public StreamList<Member> getActiveMembers() {
        if (activeMembers == null) {
            activeMembers = getAllMembers().Filter(this::memberIsActive);
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

    private final Map<String, Member> memberByIdCache = new HashMap<>();


    @Override
    public Member getMemberByUuid(String uuid) {
        Member result = memberByIdCache.get(uuid);
        if (result == null) {
            result = getAllMembers().First(member -> member.getUuid().equals(uuid));
            if (result != null) {
                memberByIdCache.put(uuid, result);
            }
        }
        return result;
    }

    @Override
    public void setMemberActive(Member memberForChangeActive, boolean active) {

        super.setMemberActive(memberForChangeActive, active);

        getActiveMembers();

        if (!active) {
            getActiveMembers().Remove(m -> m.equals(memberForChangeActive));
        } else {

            Member exist = activeMembers.First(member -> member.equals(memberForChangeActive));
            if (exist == null) {
                activeMembers.add(memberForChangeActive);
            }
        }



    }


    private StreamList<Transaction> cacheTransactions = null;

    @Override
    public StreamList<Transaction> getTransactions() {
        if (cacheTransactions == null) {
            cacheTransactions = super.getTransactions();
        }
        return cacheTransactions;
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        Transaction transaction = super.addTransaction(draftTransaction);
        cacheTransactions.add(0, transaction);
        return transaction;
    }


    private Member me = null;

    @Override
    public Member getMe() {
        if (me == null) {
            me = super.getMe();
        }
        return me;
    }
}
