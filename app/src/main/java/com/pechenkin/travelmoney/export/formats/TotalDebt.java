package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.BuildConfig;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.cost.calculation.Calculation;

import java.util.Date;

/**
 * Вернет итоги в виде текста
 */
public class TotalDebt implements ExportFormat {
    @Override
    public String getText(TripBaseTableRow pageTrip) {

        CostQueryResult allCosts = t_costs.getAllByTripId(pageTrip.id);

        StringBuilder result = new StringBuilder();

        ShortCost[] calculateCosts = Calculation.calculate(allCosts.getAllRows());

        if (t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)){
            calculateCosts = Calculation.groupByColor(calculateCosts);
        }

        if (calculateCosts.length == 0) {
            return "Долгов нет";
        }

        result.append("Итоги по поездке \"").append(pageTrip.name).append("\"\n");
        result.append("Дата: ").append(Help.dateToDateTimeStr(new Date()));

        result.append("\n\n");

        for (Cost cost : calculateCosts) {
            long memberId = cost.getMember();
            MemberBaseTableRow member = t_members.getMemberById(memberId);

            long toMemberId = cost.getToMember();
            MemberBaseTableRow toMember = t_members.getMemberById(toMemberId);

            result.append(member.name).append(" --> ").append(toMember.name).append(" ").append(Help.doubleToString(cost.getSum())).append("\n");
        }


        result.append("\nСформировано в Travel Money ").append(BuildConfig.VERSION_NAME).append("\n");
        result.append(TMConst.TM_APP_URL);

        return result.toString();
    }

    @Override
    public String getMimeType() {
        return "text/plain";
    }

    @Override
    public String getExpansionType() {
        return "";
    }

}