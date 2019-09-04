package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.transaction.TransactionItem;

public class LocalTransactionItem extends IdTableRow implements TransactionItem {



    private final long memberId;
    private Member member = null;
    private final int debit;
    private final int credit;
    private final long transactionId;


    public LocalTransactionItem(Cursor c) {
        super(c);

        this.memberId = getLongColumnValue(Namespace.FIELD_MEMBER, c);
        this.debit = getIntColumnValue(Namespace.FIELD_DEBIT, c);
        this.credit = getIntColumnValue(Namespace.FIELD_CREDIT, c);
        this.transactionId = getLongColumnValue(Namespace.FIELD_TRANSACTION, c);
    }


    @NonNull
    @Override
    public Member getMember() {
        if (member == null){
            member = TripManager.INSTANCE.getActiveTrip().getMemberById(memberId);
        }
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

    public long getTransactionId() {
        return transactionId;
    }
}
