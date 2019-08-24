package com.pechenkin.travelmoney.transaction.list;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;

import java.util.ArrayList;
import java.util.List;

public abstract class TransactionListItem implements CostListItem {

    protected Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }

    TransactionListItem(Transaction transaction) {
        this.transaction = transaction;
    }

    public static List<TransactionListItem> create(List<Transaction> transactions) {

        List<TransactionListItem> transactionListItems = new ArrayList<>(transactions.size());

        for (Transaction transaction : transactions) {
            if (transaction.getCreditItems().size() == 1 && transaction.getCreditItems().size() == 1) {
                transactionListItems.add(new OneItem(transaction));
            } else {
                transactionListItems.add(new ManyItems(transaction));
            }


        }
        return transactionListItems;

    }


    @Override
    public boolean isClicked() {
        return true;
    }

    @Override
    public boolean onLongClick() {
        this.transaction.setActive(!this.transaction.isActive());
        return true;
    }


}
