package com.pechenkin.travelmoney.bd.firestore;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.firestore.documents.MemberDocument;
import com.pechenkin.travelmoney.bd.firestore.documents.TransactionDocument;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;
import java.util.Date;

public class TripFireStore implements Trip {

    private String comment;
    private String name;
    private final String uuid;
    private final long id;


    public TripFireStore(TripTableRow trip) {

        this.comment = trip.comment;
        this.name = trip.name;
        this.uuid = trip.uuid;
        this.id = trip.id;

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
        TableTrip.INSTANCE.edit(this.id, name, comment);
        this.name = name;
        this.comment = comment;
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
        return new StreamList<>(
                TransactionDocument.INSTANCE.getTransactionsByTrip(this.uuid)
        );
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        return TransactionDocument.INSTANCE.addTransaction(this.uuid, draftTransaction);
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
