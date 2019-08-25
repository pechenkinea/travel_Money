package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.transaction.TransactionItem;

public class LocalTransactionItem extends IdTableRow implements TransactionItem {


    private Member member;
    private int debit;
    private int credit;


    public LocalTransactionItem(Cursor c) {
        super(c);

        this.member = TableMembers.INSTANCE.getMemberById(getLongColumnValue(Namespace.FIELD_MEMBER, c));
        this.debit = getIntColumnValue(Namespace.FIELD_DEBIT, c);
        this.credit = getIntColumnValue(Namespace.FIELD_CREDIT, c);

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
