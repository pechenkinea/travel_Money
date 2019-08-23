package com.pechenkin.travelmoney.page.cost.add.data;

import com.pechenkin.travelmoney.Division;
import com.pechenkin.travelmoney.bd.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pechenkin on 04.06.2018.
 */
public class CostMember {
    private final Member member;
    private boolean isChange = false;
    private int sum;

    private CostMember(Member member, int sum) {
        this.member = member;
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
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

    public static List<CostMember> createCostMemberBaseTableRow(List<Member> members, int sum) {

        Division division = new Division(sum, members.size());
        List<CostMember> result = new ArrayList<>();
        for (Member member : members) {
            result.add(new CostMember(member, division.getNext()));
        }

        return result;
    }
}
