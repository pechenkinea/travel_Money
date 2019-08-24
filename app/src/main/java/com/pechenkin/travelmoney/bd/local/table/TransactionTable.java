package com.pechenkin.travelmoney.bd.local.table;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;

//TODO реализовать все после адаптации БД
public class TransactionTable {

    public static TransactionTable INSTANCE = new TransactionTable();

    private TransactionTable(){

    }

    public Transaction[] getTransactionsByTrip(long tripId){

        return new Transaction[0];
    }

    public TransactionItem[] getTransactionItemByTransaction(long transactionId){


        return new TransactionItem[0];
    }
}
