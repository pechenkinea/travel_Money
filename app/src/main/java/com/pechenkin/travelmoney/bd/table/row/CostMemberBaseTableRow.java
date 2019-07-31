package com.pechenkin.travelmoney.bd.table.row;

/**
 * Created by pechenkin on 04.06.2018.
 */

public class CostMemberBaseTableRow {
    private final MemberBaseTableRow memberRow;
    private boolean isChange = false;
    private double sum;

    private CostMemberBaseTableRow(MemberBaseTableRow memberRow, double sum) {
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

    public MemberBaseTableRow getMemberRow() {
        return memberRow;
    }

    public static CostMemberBaseTableRow[] createCostMemberBaseTableRow(MemberBaseTableRow[] membersRows, double sum) {
        double sSum = (sum > 0 && membersRows.length > 0) ? sum / membersRows.length : 0;

        CostMemberBaseTableRow[] result = new CostMemberBaseTableRow[membersRows.length];
        for (int i = 0; i < membersRows.length; i++) {
            result[i] = new CostMemberBaseTableRow(membersRows[i], sSum);
        }

        return result;
    }
}
