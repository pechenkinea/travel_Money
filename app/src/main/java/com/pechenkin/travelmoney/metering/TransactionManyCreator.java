package com.pechenkin.travelmoney.metering;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.table.Namespace;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class TransactionManyCreator {

    private static int number = 0;

    /*
     * Добавляет в текущую поездку 1000 транзакций разом.
     */
    public static void createBigCostList() {

        final ProgressDialog progressDialog = Help.createProgressDialog(MainActivity.INSTANCE);
        progressDialog.show();

        Thread printCostThready = new Thread(() -> {

            List<Member> members = TripManager.INSTANCE.getActiveTrip().getActiveMembers();

            Random memberRandom = new Random();

            for (int i = 0; i < 1000; i++) {

                DraftTransaction draftTransaction = new DraftTransaction();

                draftTransaction.addCreditItem(new DraftTransactionItem(members.get(memberRandom.nextInt(members.size())), 0, new Random().nextInt(500_00)))
                        .addDebitItem(new DraftTransactionItem(members.get(memberRandom.nextInt(members.size())), 0, 0))
                        .addDebitItem(new DraftTransactionItem(members.get(memberRandom.nextInt(members.size())), 0, 0))
                        .addDebitItem(new DraftTransactionItem(members.get(memberRandom.nextInt(members.size())), 0, 0))
                        .setComment("comment " + number++)
                        .updateSum();

                TripManager.INSTANCE.getActiveTrip().addTransaction(draftTransaction);
            }
            progressDialog.dismiss();
        });

        printCostThready.start();


    }
}
