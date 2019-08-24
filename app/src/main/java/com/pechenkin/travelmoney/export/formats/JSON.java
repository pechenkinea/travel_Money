package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.utils.Help;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSON implements ExportFormat {
    @Override
    public String getText(Trip trip) {
        JSONObject exportJson = new JSONObject();
        try {
            exportJson.put("tripId", trip.getId());
            exportJson.put("tripName", trip.getName());


            ArrayList<Member> membersList = new ArrayList<>();

            List<Transaction> transactions = trip.getTransactions();
            JSONArray json_transactions = new JSONArray();
            if (transactions.size() > 0) {
                for (Transaction transaction : transactions) {
                    JSONObject json_transaction = new JSONObject();
                    json_transaction.put("date", (transaction.getDate() != null) ? transaction.getDate().getTime() : "");
                    json_transaction.put("sum", transaction.getSum());
                    json_transaction.put("comment", transaction.getComment());
                    json_transaction.put("active", transaction.isActive());


                    JSONArray creditItems = new JSONArray();
                    transaction.getCreditItems().ForEach(transactionItem -> {
                        JSONObject item = new JSONObject();
                        try {
                            item.put("member", transactionItem.getMember().getId());
                            item.put("sum", transactionItem.getCredit());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!membersList.contains(transactionItem.getMember()))
                            membersList.add(transactionItem.getMember());

                        creditItems.put(item);
                    });
                    json_transaction.put("creditItems", creditItems);


                    JSONArray debitItems = new JSONArray();
                    transaction.getDebitItems().ForEach(transactionItem -> {
                        JSONObject item = new JSONObject();
                        try {
                            item.put("member", transactionItem.getMember().getId());
                            item.put("sum", transactionItem.getCredit());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!membersList.contains(transactionItem.getMember()))
                            membersList.add(transactionItem.getMember());

                        debitItems.put(item);
                    });
                    json_transaction.put("debitItems", debitItems);


                    json_transactions.put(json_transaction);


                }
            }
            exportJson.put("transactions", json_transactions);


            JSONArray members = new JSONArray();
            for (Member m : membersList) {
                JSONObject member = new JSONObject();
                member.put("id", m.getId());
                member.put("name", m.getName());
                members.put(member);
            }

            exportJson.put("members", members);


        } catch (JSONException e) {
            Help.alertError("Ошибка формирования json. " + e.getMessage());
            return "";
        }

        return exportJson.toString();
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    @Override
    public String getExpansionType() {
        return ".json";
    }
}
