package com.pechenkin.travelmoney.bd.firestore.documents;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.firestore.FireBaseData;
import com.pechenkin.travelmoney.bd.firestore.MemberFireStore;
import com.pechenkin.travelmoney.bd.firestore.TransactionFireStore;
import com.pechenkin.travelmoney.bd.firestore.TransactionItemFireStore;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class TransactionDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static TransactionDocument INSTANCE = new TransactionDocument();

    private TransactionDocument() {

    }


    public Transaction addTransaction(String tripUuid, DraftTransaction draftTransaction) {

        Map<String, Object> data1 = new HashMap<>();
        data1.put("comment", draftTransaction.getComment());
        data1.put("imageUrl", draftTransaction.getImageUrl());
        data1.put("date", draftTransaction.getDate());
        data1.put("repayment", draftTransaction.isRepayment());
        data1.put("active", draftTransaction.isActive());

        data1.put("trip", tripUuid);


        String transactionUuid = UUID.randomUUID().toString();
        WriteBatch batch = db.batch();

        DocumentReference transactionRef = db.collection("trips").document(tripUuid).collection("transactions").document(transactionUuid);
        batch.set(transactionRef, data1);

        TransactionFireStore result = new TransactionFireStore(draftTransaction, transactionRef);


        StreamList.ForEach<TransactionItem> transactionItemForEach = transactionItem -> {
            Map<String, Object> dataTransactionItem = new HashMap<>();
            dataTransactionItem.put("member", ((MemberFireStore) transactionItem.getMember()).getUuid());
            dataTransactionItem.put("debit", transactionItem.getDebit());
            dataTransactionItem.put("credit", transactionItem.getCredit());
            dataTransactionItem.put("trip", tripUuid);
            dataTransactionItem.put("transaction", transactionUuid);

            batch.set(transactionRef.collection("transactionItems").document(UUID.randomUUID().toString()), dataTransactionItem);

            if (transactionItem.getCredit() > 0) {
                result.addCreditItem(new TransactionItemFireStore(transactionItem.getMember(), transactionItem.getDebit(), transactionItem.getCredit()));
            }
            if (transactionItem.getDebit() > 0) {
                result.addDebitItem(new TransactionItemFireStore(transactionItem.getMember(), transactionItem.getDebit(), transactionItem.getCredit()));
            }

        };

        draftTransaction.getCreditItems().ForEach(transactionItemForEach);
        draftTransaction.getDebitItems().ForEach(transactionItemForEach);

        FireBaseData.getSync(batch.commit());


        return result;
    }


    public List<Transaction> getTransactionsByTrip(String tripUuid) {

        Map<String, TransactionFireStore> transactionFireStoreMap = new HashMap<>();

        CollectionReference transactionCollection = db.collection("trips").document(tripUuid).collection("transactions");
        QuerySnapshot queryDocumentSnapshots = FireBaseData.getSync(transactionCollection.get());

        for (DocumentSnapshot transaction : queryDocumentSnapshots.getDocuments()) {
            transactionFireStoreMap.put(transaction.getId(), new TransactionFireStore(transaction));
        }


        Query transactions = db.collectionGroup("transactionItems").whereEqualTo("trip", tripUuid);
        QuerySnapshot transactionsItemsSnapshots = FireBaseData.getSync(transactions.get());
        for (DocumentSnapshot transactionItem : transactionsItemsSnapshots.getDocuments()) {

            TransactionItemFireStore transactionItemFireStore = new TransactionItemFireStore(transactionItem);

            String transactionUuid = Help.toString(transactionItem.get("transaction"), "");

            TransactionFireStore transaction = transactionFireStoreMap.get(transactionUuid);
            if (transaction != null) {
                if (transactionItemFireStore.getCredit() > 0) {
                    transaction.addCreditItem(transactionItemFireStore);
                }
                if (transactionItemFireStore.getDebit() > 0) {
                    transaction.addDebitItem(transactionItemFireStore);
                }
            }

        }


        List<Transaction> result = new ArrayList<>(transactionFireStoreMap.size());
        result.addAll(transactionFireStoreMap.values());

        Collections.sort(result, (left, right) -> right.getDate().compareTo(left.getDate()));


        return result;

    }

}
