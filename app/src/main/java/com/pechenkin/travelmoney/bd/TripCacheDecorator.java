package com.pechenkin.travelmoney.bd;


import androidx.collection.LongSparseArray;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;

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

    private final LongSparseArray<Member> memberByIdCache = new LongSparseArray<>();

    @Override
    public Member getMemberById(long id) {
        Member result = memberByIdCache.get(id);
        if (result == null) {
            result = getAllMembers().First(member -> member.getId() == id);
            if (result != null) {
                memberByIdCache.put(id, result);
            }
        }
        return result;
    }

    @Override
    public boolean memberIsActive(Member member) {
        return getActiveMembers().First(m -> m.equals(member)) != null;
    }

    @Override
    public void setMemberActive(Member memberForDelete, boolean active) {

        super.setMemberActive(memberForDelete, active);

        if (!active) {
            getActiveMembers().Remove(m -> m.equals(memberForDelete));
        } else if (!memberIsActive(memberForDelete)) {
            activeMembers.add(memberForDelete);
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
