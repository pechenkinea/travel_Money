package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.ArrayList;
import java.util.List;

public class CSV implements ExportFormat {
    @Override
    public String getText(Trip trip) {

        ArrayList<Member> membersList = new ArrayList<>();
        List<Transaction> transactions = trip.getTransactions();
        StringBuilder valueCosts = new StringBuilder("Операции\r\n");
        valueCosts.append("Дата;Кто;Дебет;Кредит;Активно;Возврат долга;Комментарий").append("\r\n");
        if (transactions.size() > 0) {
            for (Transaction transaction : transactions) {

                StreamList.ForEach<TransactionItem> transactionItemForEach = transactionItem -> {

                    String line = ((transaction.getDate() != null) ? Help.dateToDateTimeStr(transaction.getDate()) : "") + ";"
                            + transactionItem.getMember().getId() + ";"
                            + transactionItem.getDebit() + ";"
                            + transactionItem.getCredit() + ";"
                            + transaction.isActive() + ";"
                            + transaction.isRepayment() + ";"
                            + transaction.getComment();


                    line = line.replaceAll("\n|\n\r", " ");
                    valueCosts.append(line).append("\r\n");

                    if (!membersList.contains(transactionItem.getMember()))
                        membersList.add(transactionItem.getMember());

                };

                transaction.getCreditItems().ForEach(transactionItemForEach);
                transaction.getDebitItems().ForEach(transactionItemForEach);

            }
        }

        StringBuilder valueMembers = new StringBuilder("Участники\r\n");
        valueMembers.append("id;Имя").append("\r\n");
        for (Member member : membersList) {

            String line = member.getId() + ";" + member.getName();
            line = line.replaceAll("\n|\n\r", " ");
            valueMembers.append(line).append("\r\n");

        }
        valueMembers.append("\r\n");
        return valueMembers.append(valueCosts).toString();
    }

    @Override
    public String getMimeType() {
        return "text/csv";
    }

    @Override
    public String getExpansionType() {
        return ".csv";
    }
}
