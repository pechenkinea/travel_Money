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
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.cost.processing.CostIterable;
import com.pechenkin.travelmoney.cost.processing.ProcessIterate;
import com.pechenkin.travelmoney.cost.processing.calculation.Calculation;
import com.pechenkin.travelmoney.cost.processing.summary.AllSum;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

import java.util.Date;

/**
 * Вернет итоги в виде текста
 */
public class TotalDebt implements ExportFormat {
    @Override
    public String getText(TripBaseTableRow pageTrip) {

        CostQueryResult allCosts = t_costs.getAllByTripId(pageTrip.id);

        StringBuilder result = new StringBuilder();


        Calculation calc = new Calculation(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));
        Total total = new Total();
        AllSum allSumIteration = new AllSum();

        ProcessIterate.doIterate(allCosts.getAllRows(), new CostIterable[]{calc, total, allSumIteration});
        ShortCost[] calculateCosts = calc.getResult();
        Total.MemberSum[] totalResult = total.getResult();
        double allSum = allSumIteration.getSum();


        result.append("Итоги по поездке \"").append(pageTrip.name).append("\" (");
        result.append(Help.dateToDateStr(t_trips.getStartTripDate(pageTrip.id))).append(" - ").append(Help.dateToDateStr(t_trips.getEndTripDate(pageTrip.id))).append(")\n");

        result.append("\n\n");
        result.append("Всего потрачено: ").append(Help.doubleToString(allSum));

        result.append("\n\n");
        if (calculateCosts.length == 0) {
            return "Долгов нет";
        } else {

            result.append("Кто кому сколько должен:\n");

            for (Cost cost : calculateCosts) {
                long memberId = cost.getMember();
                MemberBaseTableRow member = t_members.getMemberById(memberId);

                long toMemberId = cost.getToMember();
                MemberBaseTableRow toMember = t_members.getMemberById(toMemberId);

                result.append(member.name).append(" --> ").append(toMember.name).append(" ").append(Help.doubleToString(cost.getSum())).append("\n");
            }
        }


        if (totalResult.length > 0) {
            result.append("\n\n");
            result.append("Кто сколько потратил:\n");

            for (Total.MemberSum mSum : totalResult) {
                MemberBaseTableRow member = t_members.getMemberById(mSum.getMemberId());
                result.append(member.name).append(": ").append(Help.doubleToString(mSum.getSumIn())).append("\n");
            }
        }


        result.append("\n");
        result.append("Сформировано в Travel Money ").append(BuildConfig.VERSION_NAME).append("\n");
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
