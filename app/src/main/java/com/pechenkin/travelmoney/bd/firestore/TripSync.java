package com.pechenkin.travelmoney.bd.firestore;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.firestore.documents.MemberDocument;
import com.pechenkin.travelmoney.bd.firestore.documents.TransactionDocument;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.TableTransaction;
import com.pechenkin.travelmoney.transaction.Transaction;

import java.util.List;

public class TripSync {

    public static void sync(String tripUuid) {

        List<Member> actualMembers = new MemberDocument().getAllMembersByUuidTrip(tripUuid);

        for (Member actualMember : actualMembers) {

            Member localMember = TableMembers.INSTANCE.getMemberByUuid(actualMember.getUuid());
            if (localMember != null) {
                localMember.edit(actualMember.getName(), actualMember.getColor(), actualMember.getIcon());

                if (actualMember.isActive() != localMember.isActive()) {
                    localMember.setActive(actualMember.isActive());
                }
            } else {
                TableMembers.INSTANCE.add(
                        actualMember.getName(),
                        actualMember.getColor(),
                        actualMember.getIcon(),
                        tripUuid,
                        actualMember.getUuid()
                );
            }
        }

        List<Transaction> actualTransactions = new TransactionDocument().getTransactionsByTrip(tripUuid);
        for (Transaction actualTransaction : actualTransactions) {
            Transaction localTransaction = TableTransaction.INSTANCE.getTransactionByUuid(actualTransaction.getUuid());
            if (localTransaction != null) {
                //TODO если будет механизм редактирования транзакций тут надо обновлять все изменения которые приехали
                localTransaction.setActive(actualTransaction.isActive());
            } else {
                TableTransaction.INSTANCE.addTransaction(tripUuid, actualTransaction);
            }
        }

    }


}
