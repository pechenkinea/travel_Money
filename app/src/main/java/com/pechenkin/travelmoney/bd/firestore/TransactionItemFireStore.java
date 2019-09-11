package com.pechenkin.travelmoney.bd.firestore;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.Help;

public class TransactionItemFireStore implements TransactionItem {

    private Member member;
    private final int debit;
    private final int credit;
    private final String uuid;
    private final String memberUuid;


    public TransactionItemFireStore(Member member, int debit, int credit, String uuid) {
        this.member = member;
        this.debit = debit;
        this.credit = credit;
        this.uuid = uuid;
        this.memberUuid = member.getUuid();
    }

    public TransactionItemFireStore(DocumentSnapshot transactionItem) {
        this.debit = (int) Help.toLong(transactionItem.getLong("debit"), 0);
        this.credit = (int) Help.toLong(transactionItem.getLong("credit"), 0);

        this.memberUuid = Help.toString(transactionItem.get("member"), "");

        this.uuid = transactionItem.getId();
    }

    @NonNull
    @Override
    public Member getMember() {
        if (member == null){
            member =  TripManager.INSTANCE.getActiveTrip().getAllMembers().First(member1 -> member1.getUuid().equals(memberUuid));
        }

        return member;
    }

    @Override
    public String getMemberUuid() {
        return memberUuid;
    }

    @Override
    public int getDebit() {
        return debit;
    }

    @Override
    public int getCredit() {
        return credit;
    }

    @Override
    public String getUuid() {
        return uuid;
    }
}
