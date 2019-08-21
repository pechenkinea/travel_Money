package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.BuildConfig;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.t_settings;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.cost.processing.CostIterable;
import com.pechenkin.travelmoney.cost.processing.ProcessIterate;
import com.pechenkin.travelmoney.cost.processing.calculation.Calculation;
import com.pechenkin.travelmoney.cost.processing.summary.AllSum;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

/**
 * Вернет итоги в виде текста
 */
public class TotalDebt implements ExportFormat {
    @Override
    public String getText(Trip trip) {

        Cost[] allCosts = trip.getAllCost();

        StringBuilder result = new StringBuilder();


        Calculation calc = new Calculation(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));
        Total total = new Total();
        AllSum allSumIteration = new AllSum();

        ProcessIterate.doIterate(allCosts, new CostIterable[]{calc, total, allSumIteration});
        ShortCost[] calculateCosts = calc.getResult();
        Total.MemberSum[] totalResult = total.getResult();
        double allSum = allSumIteration.getSum();


        result.append("Итоги по поездке \"").append(trip.getName()).append("\" (");
        result.append(Help.dateToDateStr(trip.getStartDate())).append(" - ").append(Help.dateToDateStr(trip.getEndDate())).append(")\n");

        result.append("\n\n");
        result.append("Всего потрачено: ").append(Help.doubleToString(allSum));

        result.append("\n\n");
        if (calculateCosts.length == 0) {
            return "Долгов нет";
        } else {

            result.append("Кто кому сколько должен:\n");

            for (Cost cost : calculateCosts) {
                Member member = cost.getMember();
                Member toMember =  cost.getToMember();

                result.append(member.getName()).append(" --> ").append(toMember.getName()).append(" ").append(Help.doubleToString(cost.getSum())).append("\n");
            }
        }


        if (totalResult.length > 0) {
            result.append("\n\n");
            result.append("Кто сколько потратил:\n");

            for (Total.MemberSum mSum : totalResult) {
                Member member = mSum.getMember();
                result.append(member.getName()).append(": ").append(Help.doubleToString(mSum.getSumExpense())).append("\n");
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
