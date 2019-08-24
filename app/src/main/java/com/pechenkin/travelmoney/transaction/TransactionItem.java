package com.pechenkin.travelmoney.transaction;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;

public interface TransactionItem {

    @NonNull
    Member getMember();

    int getDebit();

    int getCredit();

}
