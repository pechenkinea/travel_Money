package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.CostBaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;

import java.util.ArrayList;

public class CSV implements ExportFormat {
    @Override
    public String getText(TripBaseTableRow pageTrip) {

        ArrayList<Long> membersList = new ArrayList<>();
        CostQueryResult costs = t_costs.getAllByTripId(pageTrip.id);
        StringBuilder valueCosts = new StringBuilder("Операции\r\n");
        valueCosts.append("Дата;Кто;Кому;Сколько;Активно;Комментарий").append("\r\n");
        if (costs.hasRows()) {
            for (CostBaseTableRow cost : costs.getAllRows()) {
                String line = ((cost.getDate() != null) ? Help.dateToDateTimeStr(cost.getDate()) : "") + ";"
                        + cost.getMember() + ";"
                        + cost.getToMember() + ";"
                        + String.valueOf(cost.getSum()).replace('.', ',') + ";"
                        + cost.isActive() + ";"
                        + cost.getComment();
                line = line.replaceAll("\n|\n\r", " ");
                valueCosts.append(line).append("\r\n");

                if (!membersList.contains(cost.getMember()))
                    membersList.add(cost.getMember());

                if (!membersList.contains(cost.getToMember()))
                    membersList.add(cost.getToMember());
            }
        }

        StringBuilder valueMembers = new StringBuilder("Участники\r\n");
        valueMembers.append("id;Имя").append("\r\n");
        for (Long m : membersList) {

            BaseTableRow findMember = t_members.getMemberById(m);
            String line = m + ";" + ((findMember != null) ? findMember.name : "ErrorMemberName");
            line = line.replaceAll("\n|\n\r", " ");
            valueMembers.append(line).append("\r\n");

        }
        valueMembers.append("\r\n");
        return valueMembers.append(valueCosts).toString();
    }
}
