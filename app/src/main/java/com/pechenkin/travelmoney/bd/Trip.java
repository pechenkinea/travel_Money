package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.cost.Cost;

import java.util.Date;

public interface Trip {

    long getId();

    String getName();

    String getComment();

    void edit(String name, String comment);

    Member[] getAllMembers();

    Member[] getActiveMembers();

    boolean memberIsActive(Member member);

    void addMember(Member member);

    void removeMember(Member member);

    void addCost(long member_id, long to_member_id, String comment, double sum, String image_dir, Date date, boolean isRepayment);

    boolean isActive();

    Cost[] getAllCost();

}
