package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.CostBaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSON implements ExportFormat {
    @Override
    public String getText(TripBaseTableRow pageTrip) {
        JSONObject exportJson = new JSONObject();
        try {
            exportJson.put("tripId", pageTrip.id);
            exportJson.put("tripName", pageTrip.name);


            ArrayList<Long> membersList = new ArrayList<>();

            CostQueryResult costs = t_costs.getAllByTripId(pageTrip.id);
            JSONArray json_costs = new JSONArray();
            if (costs.hasRows()) {
                for (CostBaseTableRow cost : costs.getAllRows()) {
                    JSONObject json_cost = new JSONObject();
                    json_cost.put("date", (cost.date() != null) ? cost.date().getTime() : "");
                    json_cost.put("member", cost.member());
                    json_cost.put("to_member", cost.to_member());
                    json_cost.put("sum", cost.sum());
                    json_cost.put("comment", cost.comment());
                    json_cost.put("active", cost.active());
                    json_costs.put(json_cost);

                    if (!membersList.contains(cost.member()))
                        membersList.add(cost.member());

                    if (!membersList.contains(cost.to_member()))
                        membersList.add(cost.to_member());

                }
            }
            exportJson.put("costs", json_costs);


            JSONArray members = new JSONArray();
            for (long m : membersList) {
                JSONObject member = new JSONObject();
                member.put("id", m);
                BaseTableRow findMember = t_members.getMemberById(m);
                member.put("name", (findMember != null) ? findMember.name : "ErrorMemberName");
                members.put(member);
            }

            exportJson.put("members", members);


        } catch (JSONException e) {
            Help.alert("Ошибка формирования json. " + e.getMessage());
            return "";
        }

        return exportJson.toString();
    }
}