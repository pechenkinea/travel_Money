package com.pechenkin.travelmoney.transaction.draft;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.TransactionItem;

public class DaftTransactionItem implements TransactionItem {

    private Member member;
    int debit;
    private int credit;
    private boolean isChange = false;
    private DraftUpdateListener updateListener = null;


    public DaftTransactionItem(Member member, int debit, int credit) {
        this.member = member;
        this.debit = debit;
        this.credit = credit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
        isChange = true;
        if (updateListener != null) {
            updateListener.update();
        }
    }

    public void addCredit(int credit) {
        this.credit += credit;
    }

    public boolean isChange() {
        return isChange;
    }

    @NonNull
    @Override
    public Member getMember() {
        return this.member;
    }

    @Override
    public int getDebit() {
        return this.debit;
    }

    @Override
    public int getCredit() {
        return this.credit;
    }

    public DaftTransactionItem setUpdateListener(DraftUpdateListener draftUpdateListener) {
        this.updateListener = draftUpdateListener;
        return this;
    }
}
