package com.pechenkin.travelmoney.export.formats;

import android.util.SparseArray;

import com.pechenkin.travelmoney.BuildConfig;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.TableSettings;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.transaction.processing.CostIterable;
import com.pechenkin.travelmoney.transaction.processing.ProcessIterate;
import com.pechenkin.travelmoney.transaction.processing.calculation.Calculation;
import com.pechenkin.travelmoney.transaction.processing.summary.AllSum;
import com.pechenkin.travelmoney.transaction.processing.summary.Total;
import com.pechenkin.travelmoney.utils.Help;

import java.util.ArrayList;
import java.util.List;

/**
 * Вернет итоги в виде текста
 */
public class TotalDebt implements ExportFormat {
    @Override
    public String getText(Trip trip) {

        List<Transaction> allTransactions = trip.getTransactions();

        StringBuilder result = new StringBuilder();


        Calculation calc = new Calculation(TableSettings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));
        Total total = new Total();
        AllSum allSumIteration = new AllSum();

        ProcessIterate.doIterate(allTransactions, new CostIterable[]{calc, total, allSumIteration});
        List<CostListItem> calculateCosts = calc.getResult();
        List<Total.MemberSum> totalResult = total.getResult();
        int allSum = allSumIteration.getSum();


        result.append("Итоги по поездке \"").append(trip.getName()).append("\" (");
        result.append(Help.dateToDateStr(trip.getStartDate())).append(" - ").append(Help.dateToDateStr(trip.getEndDate())).append(")\n");

        result.append("\n\n");
        result.append("Всего потрачено: ").append(Help.kopToTextRub(allSum));

        result.append("\n\n");
        if (calculateCosts.size() == 0) {
            return "Долгов нет";
        } else {

            result.append("Кто кому сколько должен:\n");

            for (CostListItem totalItem : calculateCosts) {
                result.append(totalItem.toString()).append("\n");
            }
        }

        // если есть группировка по цвету то в итог пишем кто имеет общий бюджет
        if (TableSettings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)) {

            SparseArray<List<Member>> membersGroupByColor = new SparseArray<>();

            for (Total.MemberSum memberSum : totalResult) {
                List<Member> colorMemberList = membersGroupByColor.get(memberSum.getMember().getColor());
                if (colorMemberList == null) {
                    colorMemberList = new ArrayList<>();
                    membersGroupByColor.put(memberSum.getMember().getColor(), colorMemberList);
                }
                colorMemberList.add(memberSum.getMember());
            }

            StringBuilder groupColorText = new StringBuilder();
            for (int i = 0; i < membersGroupByColor.size(); i++) {
                List<Member> colorMemberList = membersGroupByColor.valueAt(i);
                if (colorMemberList.size() > 1) {

                    groupColorText.append(colorMemberList.get(0).getName());

                    for (int mIndex = 1; mIndex < colorMemberList.size(); mIndex++){
                        Member member = colorMemberList.get(mIndex);

                        groupColorText.append(" + ").append(member.getName());
                    }

                    groupColorText.append("\n");
                }
            }


            if (groupColorText.length() > 0){
                result.append("\n\nОбщий бюджет:\n").append(groupColorText);
            }
        }


        if (totalResult.size() > 0) {
            result.append("\n\n");
            result.append("Кто сколько потратил:\n");

            for (Total.MemberSum mSum : totalResult) {
                Member member = mSum.getMember();
                result.append(member.getName()).append(": ").append(Help.kopToTextRub(mSum.getSumExpense())).append("\n");
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
