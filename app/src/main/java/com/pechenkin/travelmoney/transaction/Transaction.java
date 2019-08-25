package com.pechenkin.travelmoney.transaction;

import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.Date;

public interface Transaction {

    long getId();

    Date getDate();

    String getComment();

    StreamList<TransactionItem> getDebitItems();

    StreamList<TransactionItem> getCreditItems();

    String getImageUrl();

    boolean isActive();

    void setActive(boolean value);

    int getSum();

    /**
     * Если true то операция является возвратом долга
     */
    boolean isRepayment();

}
