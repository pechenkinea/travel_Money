package com.pechenkin.travelmoney.transaction.draft;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.TransactionItem;

import java.util.UUID;

public class DraftTransactionItem implements TransactionItem {

    private Member member;
    int debit;
    private int credit;
    private boolean isChange = false;
    private DraftUpdateListener updateListener = null;
    private final String uuid = UUID.randomUUID().toString();


    public DraftTransactionItem(Member member, int debit, int credit) {
        this.member = member;
        this.debit = debit;
        this.credit = credit;
    }

    public DraftTransactionItem setDebit(int debit) {
        this.debit = debit;
        isChange = true;
        if (updateListener != null) {
            updateListener.update();
        }
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setChange(boolean change) {
        isChange = change;
        if (updateListener != null) {
            updateListener.update();
        }
    }

    public void setCredit(int credit) {
        this.credit = credit;
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
    public String getMemberUuid() {
        return getMember().getUuid();
    }

    @Override
    public int getDebit() {
        return this.debit;
    }

    @Override
    public int getCredit() {
        return this.credit;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public DraftTransactionItem setUpdateListener(DraftUpdateListener draftUpdateListener) {
        this.updateListener = draftUpdateListener;
        return this;
    }
}
