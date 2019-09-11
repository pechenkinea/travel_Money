package com.pechenkin.travelmoney.transaction;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;

public interface TransactionItem {

    Member getMember();

    String getMemberUuid();

    int getDebit();

    int getCredit();

    String getUuid();

}
