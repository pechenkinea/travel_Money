package com.pechenkin.travelmoney.transaction;

import java.util.Date;
import java.util.List;

public interface Transaction {

    long getId();

    Date getDate();

    String getComment();

    List<TransactionItem> getDebitItems();

    List<TransactionItem> getCreditItems();

    String getImageUrl();

    boolean isActive();

    void setActive(boolean value);

    //TODO оно тут надо?
    int getSum();

    /**
     * Если true то операция является возвратом долга
     */
    boolean isRepayment();

}
