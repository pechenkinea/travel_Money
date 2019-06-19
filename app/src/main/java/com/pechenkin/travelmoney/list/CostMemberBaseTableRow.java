package com.pechenkin.travelmoney.list;

import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;

/**
 * Created by pechenkin on 04.06.2018.
 *
 */

public class CostMemberBaseTableRow {
    private MemberBaseTableRow memberRow;
    private  boolean isChange = false;
    private  double sum;

    public  CostMemberBaseTableRow(MemberBaseTableRow memberRow, double sum)
    {
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

    public static CostMemberBaseTableRow[] createCostMemberBaseTableRow(MemberBaseTableRow[] membersRows, double sum)
    {
        double ssum = (sum > 0 && membersRows.length > 0)?sum/membersRows.length:0;

        CostMemberBaseTableRow[] result = new CostMemberBaseTableRow[membersRows.length];
        for (int i=0; i< membersRows.length; i++) {
            result[i] = new CostMemberBaseTableRow(membersRows[i], ssum);
        }

        return result;
    }
}
