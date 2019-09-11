package com.pechenkin.travelmoney.bd.firestore.documents.cache;

import com.google.firebase.firestore.DocumentReference;
import com.pechenkin.travelmoney.bd.firestore.TransactionFireStore;
import com.pechenkin.travelmoney.bd.firestore.TransactionItemFireStore;
import com.pechenkin.travelmoney.bd.firestore.documents.TransactionDocument;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableTransaction;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionLocalCache extends TransactionDocument {

    @Override
    public Transaction addTransaction(String tripUuid, DraftTransaction draftTransaction) {
        Transaction result = super.addTransaction(tripUuid, draftTransaction);

        TripTableRow tripLocal = TableTrip.INSTANCE.getByUuid(tripUuid);
        TableTransaction.INSTANCE.addTransaction(tripLocal.uuid, draftTransaction);

        return result;
    }


    @Override
    public List<Transaction> getTransactionsByTrip(String tripUuid) {

        TripTableRow tripLocal = TableTrip.INSTANCE.getByUuid(tripUuid);
        List<Transaction> localTransactions = TableTransaction.INSTANCE.getTransactionsByTrip(tripLocal.uuid);

        List<Transaction> resultList = new ArrayList<>(localTransactions.size());
        for (Transaction transaction : localTransactions) {

            DocumentReference transactionRef = db.collection("trips").document(tripUuid).collection("transactions").document(transaction.getUuid());
            TransactionFireStore transactionFireStore = new TransactionFireStore(transaction, transactionRef);

            transaction.getCreditItems().ForEach(transactionItem ->
                    transactionFireStore.addCreditItem(new TransactionItemFireStore(
                            transactionItem.getMember(),
                            transactionItem.getDebit(),
                            transactionItem.getCredit(),
                            transactionItem.getUuid()
                    )));
            transaction.getDebitItems().ForEach(transactionItem ->
                    transactionFireStore.addDebitItem(new TransactionItemFireStore(
                            transactionItem.getMember(),
                            transactionItem.getDebit(),
                            transactionItem.getCredit(),
                            transactionItem.getUuid()
                    )));

            resultList.add(transactionFireStore);
        }

        return resultList;

    }
}
