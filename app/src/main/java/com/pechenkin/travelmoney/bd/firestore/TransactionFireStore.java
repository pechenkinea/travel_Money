package com.pechenkin.travelmoney.bd.firestore;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.Date;

public class TransactionFireStore implements Transaction {

    private static int idCounter = 0;

    private final DocumentReference reference;
    private final int id;
    private final Date date;
    private final String comment;
    private final String imageUrl;
    private final String uuid;
    private boolean active;
    private boolean repayment;

    private final StreamList<TransactionItem> creditItems = new StreamList<>(new ArrayList<>());
    private final StreamList<TransactionItem> debitItems = new StreamList<>(new ArrayList<>());


    public TransactionFireStore(Transaction draftTransaction, DocumentReference reference) {

        this.id = idCounter++;
        this.reference = reference;

        this.date = draftTransaction.getDate();
        this.comment = draftTransaction.getComment();
        this.imageUrl = draftTransaction.getImageUrl();
        this.active = draftTransaction.isActive();
        this.repayment = draftTransaction.isRepayment();
        this.uuid = reference.getId();

    }

    public TransactionFireStore(DocumentSnapshot transaction) {
        this.id = idCounter++;
        this.reference = transaction.getReference();

        this.date =  Help.toDate(transaction.getDate("date"), new Date());
        this.comment = Help.toString(transaction.get("comment"), "");
        this.imageUrl = Help.toString(transaction.get("imageUrl"), "");
        this.active =  Help.toBoolean(transaction.getBoolean("active"), true);
        this.repayment =  Help.toBoolean(transaction.getBoolean("repayment"), false);

        this.uuid = reference.getId();

    }

    public void addCreditItem(TransactionItemFireStore transactionItemFireStore) {
        creditItems.add(transactionItemFireStore);
    }

    public void addDebitItem(TransactionItemFireStore transactionItemFireStore) {
        debitItems.add(transactionItemFireStore);
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public StreamList<TransactionItem> getDebitItems() {
        return debitItems;
    }

    @Override
    public StreamList<TransactionItem> getCreditItems() {
        return creditItems;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean value) {
        this.reference.update("active", value);
        this.active = value;
    }

    @Override
    public int getSum() {
        int sum = 0;
        for (TransactionItem transactionItem : getCreditItems()) {
            sum += transactionItem.getCredit();
        }

        return sum;
    }

    @Override
    public boolean isRepayment() {
        return repayment;
    }
}
