package com.pechenkin.travelmoney.page.cost.add.data;

import com.pechenkin.travelmoney.bd.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pechenkin on 04.06.2018.
 */
public class CostMember {
    private final Member member;
    private boolean isChange = false;
    private double sum;

    private CostMember(Member member, double sum) {
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public static List<CostMember> createCostMemberBaseTableRow(List<Member> members, double sum) {
        double oneMemberSum = (sum > 0 && members.size() > 0) ? sum / members.size() : 0;

        List<CostMember> result = new ArrayList<>();
        for (Member member : members) {
            result.add(new CostMember(member, oneMemberSum));
        }

        return result;
    }
}
