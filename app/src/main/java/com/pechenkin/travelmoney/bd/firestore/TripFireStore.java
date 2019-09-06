package com.pechenkin.travelmoney.bd.firestore;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.firestore.documents.MemberDocument;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;

public class TripFireStore implements Trip {

    private final String comment;
    private final String name;
    private final String uuid;

    public TripFireStore(String uuid, String name, String comment) {
        this.comment = comment;
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void edit(String name, String comment) {
        //TODO реализовать
    }

    @Override
    public Date getStartDate() {
        return new Date(); //TODO реализовать
    }

    @Override
    public Date getEndDate() {
        return new Date(); //TODO реализовать
    }

    @Override
    public StreamList<Member> getAllMembers() {
        return new StreamList<>(
                MemberDocument.INSTANCE.getAllMembersByUuidTrip(this.uuid)
        );
    }

    @Override
    public StreamList<Member> getActiveMembers() {
        return getAllMembers().Filter(member -> ((MemberFireStore) member).isActive());
    }

    @Override
    public Member createMember(String name, int color, int icon) {
        return MemberDocument.INSTANCE.add(uuid, name, color, icon);
    }

    @Override
    public Member getMe() {
        return getAllMembers().First(); //TODO надо придумать как брать текущего участника
    }

    @Override
    public Member getMemberByName(String name) {
        return getAllMembers().First(member -> member.getName().equals(name));
    }

    @Override
    public Member getMemberById(long id) {
        return getAllMembers().First(member -> member.getId() == id);
    }

    @Override
    public boolean memberIsActive(Member member) {
        return ((MemberFireStore) member).isActive();
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        ((MemberFireStore) member).setActive(active);
    }

    @Override
    public StreamList<Transaction> getTransactions() {
        return new StreamList<>(new ArrayList<>()); //TODO реализовать
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        return null; //TODO реализовать
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Trip) {
            return uuid.equals(((Trip) obj).getUUID());
        }
        return false;
    }
}
