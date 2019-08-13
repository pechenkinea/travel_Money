package com.pechenkin.travelmoney.page.cost.add.data;

import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;

/**
 * Created by pechenkin on 04.06.2018.
 */
public class CostMember {
    private final long memberId;
    private boolean isChange = false;
    private double sum;

    private CostMember(long memberId, double sum) {
        this.memberId = memberId;
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;

    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean value) {
        isChange = value;
    }

    public long getMemberId() {
        return memberId;
    }

    public static CostMember[] createCostMemberBaseTableRow(MemberTableRow[] membersRows, double sum) {
        double oneMemberSum = (sum > 0 && membersRows.length > 0) ? sum / membersRows.length : 0;

        CostMember[] result = new CostMember[membersRows.length];
        for (int i = 0; i < membersRows.length; i++) {
            result[i] = new CostMember(membersRows[i].id, oneMemberSum);
        }

        return result;
    }
}
