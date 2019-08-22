package com.pechenkin.travelmoney.test;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.cost.Cost;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripForTest implements Trip {

    private List<Member> members = new ArrayList<>();
    private List<Cost> allCost = new ArrayList<>();

    public TripForTest() {

        for (TestMembers tm : TestMembers.values()) {
            members.add(tm.getMember());
        }
    }

    @Override
    public List<Member> getAllMembers() {
        return members;
    }

    @Override
    public List<Member> getActiveMembers() {
        return members;
    }

    @Override
    public List<Cost> getAllCost() {
        return allCost;
    }

    public void addCost(Cost cost) {
        allCost.add(cost);
    }


    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getComment() {
        return "";
    }

    @Override
    public void edit(String name, String comment) {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Date getStartDate() {
        return new Date();
    }

    @Override
    public Date getEndDate() {
        return new Date();
    }


    @Override
    public Member createMember(String name, int color, int icon) {
        return null;
    }

    @Override
    public Member getMe() {
        return members.get(0);
    }


    @Override
    public Member getMemberByName(String name) {
        return null;
    }

    @Override
    public boolean memberIsActive(Member member) {
        return false;
    }

    @Override
    public void setMemberActive(Member member, boolean active) {

    }


    @Override
    public void addCost(Member member, Member toMember, String comment, double sum, String image_dir, Date date, boolean isRepayment) {

    }
}
