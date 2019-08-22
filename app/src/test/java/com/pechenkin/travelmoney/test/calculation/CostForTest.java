package com.pechenkin.travelmoney.test.calculation;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CostForTest implements Cost {

    private Member member;
    private Member toMember;
    private double sum;

    public CostForTest(Member member, Member toMember, double sum){
        this.member = member;
        this.toMember = toMember;
        this.sum = sum;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public Member getMember() {
        return member;
    }

    @Override
    public Member getToMember() {
        return toMember;
    }

    @Override
    public double getSum() {
        return sum;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean isRepayment() {
        return false;
    }

    @Override
    public void setActive(boolean value) {

    }

    @Override
    public String getImageDir() {
        return "";
    }

    @Override
    public Date getDate() {
        return new Date();
    }

    @Override
    public String getComment() {
        return "";
    }
}
