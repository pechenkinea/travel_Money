package com.pechenkin.travelmoney.bd.firestore;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.TripStore;
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
    private final TripStore tripStore;


    public TripFireStore(TripTableRow trip) {

        this.comment = trip.comment;
        this.name = trip.name;
        this.uuid = trip.uuid;
        this.id = trip.id;
        this.tripStore = trip.tripStore;

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
        TableTrip.INSTANCE.edit(this.uuid, name, comment);
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
                MemberDocument.getInstance().getAllMembersByUuidTrip(this.uuid)
        );
    }

    @Override
    public StreamList<Member> getActiveMembers() {
        return getAllMembers().Filter(member -> ((MemberFireStore) member).isActive());
    }

    @Override
    public Member createMember(String name, int color, int icon) {
        return MemberDocument.getInstance().add(uuid, name, color, icon);
    }

    @Override
    public Member getMe() {
        return getAllMembers().First(); //TODO надо придумать как брать текущего участника
    }

    @Override
    public Member getMemberByName(String memberName) {
        return getAllMembers().First(member -> member.getName().equals(memberName));
    }

    @Override
    public Member getMemberByUuid(String memberUuid) {
        return getAllMembers().First(member -> member.getUuid().equals(memberUuid));
    }


    @Override
    public boolean memberIsActive(Member member) {
        return member.isActive();
    }

    @Override
    public void setMemberActive(Member member, boolean active) {
        member.setActive(active);
    }

    @Override
    public TripStore getTripStore() {
        return this.tripStore;
    }

    @Override
    public StreamList<Transaction> getTransactions() {
        return new StreamList<>(
                TransactionDocument.getInstance().getTransactionsByTrip(this.uuid)
        );
    }

    @Override
    public Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException {
        return TransactionDocument.getInstance().addTransaction(this.uuid, draftTransaction);
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
