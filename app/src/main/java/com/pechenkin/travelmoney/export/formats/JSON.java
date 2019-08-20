package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.cost.Cost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSON implements ExportFormat {
    @Override
    public String getText(Trip trip) {
        JSONObject exportJson = new JSONObject();
        try {
            exportJson.put("tripId", trip.getId());
            exportJson.put("tripName", trip.getName());


            ArrayList<Member> membersList = new ArrayList<>();

            Cost[] costs = trip.getAllCost();
            JSONArray json_costs = new JSONArray();
            if (costs.length > 0) {
                for (Cost cost : costs) {
                    JSONObject json_cost = new JSONObject();
                    json_cost.put("date", (cost.getDate() != null) ? cost.getDate().getTime() : "");
                    json_cost.put("member", cost.getMember());
                    json_cost.put("to_member", cost.getToMember());
                    json_cost.put("sum", cost.getSum());
                    json_cost.put("comment", cost.getComment());
                    json_cost.put("active", cost.isActive());
                    json_costs.put(json_cost);

                    if (!membersList.contains(cost.getMember()))
                        membersList.add(cost.getMember());

                    if (!membersList.contains(cost.getToMember()))
                        membersList.add(cost.getToMember());

                }
            }
            exportJson.put("costs", json_costs);


            JSONArray members = new JSONArray();
            for (Member m : membersList) {
                JSONObject member = new JSONObject();
                member.put("id", m.getId());
                member.put("name", m.getName());
                members.put(member);
            }

            exportJson.put("members", members);


        } catch (JSONException e) {
            Help.alert("Ошибка формирования json. " + e.getMessage());
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
