package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_members;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.ArrayList;

public class CSV implements ExportFormat {
    @Override
    public String getText(Trip trip) {

        ArrayList<Long> membersList = new ArrayList<>();
        Cost[] costs = trip.getAllCost();
        StringBuilder valueCosts = new StringBuilder("Операции\r\n");
        valueCosts.append("Дата;Кто;Кому;Сколько;Активно;Комментарий").append("\r\n");
        if (costs.length > 0) {
            for (Cost cost : costs) {
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

            IdAndNameTableRow findMember = t_members.getMemberById(m);
            String line = m + ";" + ((findMember != null) ? findMember.name : "ErrorMemberName");
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
