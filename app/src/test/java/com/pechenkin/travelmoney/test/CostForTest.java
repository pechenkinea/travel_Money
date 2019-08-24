package com.pechenkin.travelmoney.test;

import com.pechenkin.travelmoney.bd.Member;

import java.util.Date;

public class CostForTest implements Cost {

    private Member member;
    private Member toMember;
    private int sum;

    public CostForTest(Member member, Member toMember, int sum){
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
    public int getSum() {
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
