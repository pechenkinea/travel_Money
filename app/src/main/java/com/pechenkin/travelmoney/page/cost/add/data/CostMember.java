package com.pechenkin.travelmoney.page.cost.add.data;

import com.pechenkin.travelmoney.bd.Member;

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

    public static CostMember[] createCostMemberBaseTableRow(Member[] members, double sum) {
        double oneMemberSum = (sum > 0 && members.length > 0) ? sum / members.length : 0;

        CostMember[] result = new CostMember[members.length];
        for (int i = 0; i < members.length; i++) {
            result[i] = new CostMember(members[i].getId(), oneMemberSum);
        }

        return result;
    }
}
