package com.pechenkin.travelmoney.bd.firestore;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Date;

public class TransactionItemFireStore implements TransactionItem {

    private final Member member;
    private final int debit;
    private final int credit;


    public TransactionItemFireStore(Member member, int debit, int credit) {
        this.member = member;
        this.debit = debit;
        this.credit = credit;
    }

    public TransactionItemFireStore(DocumentSnapshot transactionItem) {
        this.debit = (int) Help.toLong(transactionItem.getLong("debit"), 0);
        this.credit = (int) Help.toLong(transactionItem.getLong("credit"), 0);

        String memberUuid = Help.toString(transactionItem.get("member"), "");
        this.member = TripManager.INSTANCE.getActiveTrip().getAllMembers().First(member1 -> ((MemberFireStore) member1).getUuid().equals(memberUuid));
    }

    @NonNull
    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public int getDebit() {
        return debit;
    }

    @Override
    public int getCredit() {
        return credit;
    }
}
