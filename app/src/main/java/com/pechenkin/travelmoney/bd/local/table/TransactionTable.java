package com.pechenkin.travelmoney.bd.local.table;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;

//TODO реализовать все после адаптации БД
public class TransactionTable {

    public static TransactionTable INSTANCE = new TransactionTable();

    private TransactionTable(){

    }

    public static void addTransaction(long tripId, DraftTransaction draftTransaction) {

    }

    public Transaction[] getTransactionsByTrip(long tripId){

        // LocalTransaction
        return new Transaction[0];
    }

    public TransactionItem[] getTransactionItemByTransaction(long transactionId){


        return new TransactionItem[0];
    }
}
