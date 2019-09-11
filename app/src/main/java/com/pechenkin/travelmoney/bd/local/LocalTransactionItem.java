package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.transaction.TransactionItem;

public class LocalTransactionItem extends IdTableRow implements TransactionItem {



    private final String memberUuid;
    private Member member = null;
    private final int debit;
    private final int credit;
    private final String transactionUuid;
    private final String uuid;


    public LocalTransactionItem(Cursor c) {
        super(c);

        this.memberUuid = getStringColumnValue(Namespace.FIELD_MEMBER_UUID, c);
        this.debit = getIntColumnValue(Namespace.FIELD_DEBIT, c);
        this.credit = getIntColumnValue(Namespace.FIELD_CREDIT, c);
        this.transactionUuid = getStringColumnValue(Namespace.FIELD_TRANSACTION_UUID, c);
        this.uuid = getStringColumnValue(Namespace.FIELD_UUID, c);
    }


    @NonNull
    @Override
    public Member getMember() {
        if (member == null){
            member = TripManager.INSTANCE.getActiveTrip().getMemberByUuid(memberUuid);
            if (member == null){
                member = TableMembers.INSTANCE.getMemberByUuid(memberUuid);
            }
        }
        return member;
    }

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

    public String getTransactionUuid() {
        return transactionUuid;
    }
}
