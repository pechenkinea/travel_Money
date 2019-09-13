package com.pechenkin.travelmoney.bd.firestore;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.firestore.documents.MemberDocument;
import com.pechenkin.travelmoney.bd.firestore.documents.TransactionDocument;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.bd.local.table.TableTransaction;
import com.pechenkin.travelmoney.transaction.Transaction;

import java.util.List;

public class TripSync {

    public static String sync(String tripUuid) {

        StringBuilder result = new StringBuilder();

        List<Member> actualMembers = new MemberDocument().getAllMembersByUuidTrip(tripUuid);

        int updateMembers = 0;
        for (Member actualMember : actualMembers) {

            Member localMember = TableMembers.INSTANCE.getMemberByUuid(actualMember.getUuid());
            if (localMember != null) {
                if (memberHasChange(actualMember, localMember)) {
                    localMember.edit(actualMember.getName(), actualMember.getColor(), actualMember.getIcon());
                    localMember.setActive(actualMember.isActive());
                    updateMembers++;
                }

            } else {
                TableMembers.INSTANCE.add(
                        actualMember.getName(),
                        actualMember.getColor(),
                        actualMember.getIcon(),
                        tripUuid,
                        actualMember.getUuid()
                );
                updateMembers++;
            }
        }


        int updateTransactions = 0;
        List<Transaction> actualTransactions = new TransactionDocument().getTransactionsByTrip(tripUuid);
        for (Transaction actualTransaction : actualTransactions) {
            Transaction localTransaction = TableTransaction.INSTANCE.getTransactionByUuid(actualTransaction.getUuid());
            if (localTransaction != null) {
                //TODO если будет механизм редактирования транзакций тут надо обновлять все изменения которые приехали

                if (localTransaction.isActive() != actualTransaction.isActive()) {
                    localTransaction.setActive(actualTransaction.isActive());
                    updateTransactions++;
                }
            } else {
                TableTransaction.INSTANCE.addTransaction(tripUuid, actualTransaction);
                updateTransactions++;
            }
        }

        result.append("Обновлено участников: ").append(updateMembers).append("\n");
        result.append("Обновлено трат: ").append(updateTransactions);


        return result.toString();

    }

    private static boolean memberHasChange(Member actualMember, Member localMember){
        return !actualMember.getName().equals(localMember.getName()) ||
                actualMember.getColor() != localMember.getColor() ||
                actualMember.getIcon() != localMember.getIcon() ||
                actualMember.isActive() != localMember.isActive();


    }


}
