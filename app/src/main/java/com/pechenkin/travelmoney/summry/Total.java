package com.pechenkin.travelmoney.summry;

import com.pechenkin.travelmoney.bd.table.row.CostBaseTableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pechenkin on 13.06.2018.
 * Расчет общей статистики
 */

public class Total {

    public static Summary[] getSummary(CostBaseTableRow[] costs) {
        List<Summary> result = new ArrayList<>();

        HashMap<Long, MemberSum> members = new HashMap<>();

        for (CostBaseTableRow cost:costs) {

            if (cost.active() == 0 )
            {
                continue;
            }

            if (!members.containsKey(cost.member()))
                members.put(cost.member(), new MemberSum(cost.member()));

            members.get(cost.member()).addSumOut(cost.sum());

            if (!members.containsKey(cost.to_member()))
                members.put(cost.to_member(), new MemberSum(cost.to_member()));

            members.get(cost.to_member()).addSumIn(cost.sum());
        }

        for (MemberSum value : members.values()) {
            result.add(new Summary(value.id, value.sumIn, value.sumOut));
        }

        return result.toArray(new Summary[0]);
    }



    private static class MemberSum{
        long id;
        double sumIn = 0f;
        double sumOut = 0f;
        MemberSum(long id)
        {
            this.id = id;
        }

        void addSumIn(double sum)
        {
            this.sumIn += sum;
        }
        void addSumOut(double sum)
        {
            this.sumOut += sum;
        }


    }
}
