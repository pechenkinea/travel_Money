package com.pechenkin.travelmoney.page.cost.add.master;

import com.pechenkin.travelmoney.bd.table.query.member.MemberTableRow;

/**
 * Created by pechenkin on 04.06.2018.
 */
//TODO переписать это и все, что с этим связано
public class CostMember {
    private final MemberTableRow memberRow;
    private boolean isChange = false;
    private double sum;

    private CostMember(MemberTableRow memberRow, double sum) {
        this.memberRow = memberRow;
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

    public MemberTableRow getMemberRow() {
        return memberRow;
    }

    public static CostMember[] createCostMemberBaseTableRow(MemberTableRow[] membersRows, double sum) {
        double sSum = (sum > 0 && membersRows.length > 0) ? sum / membersRows.length : 0;

        CostMember[] result = new CostMember[membersRows.length];
        for (int i = 0; i < membersRows.length; i++) {
            result[i] = new CostMember(membersRows[i], sSum);
        }

        return result;
    }
}
