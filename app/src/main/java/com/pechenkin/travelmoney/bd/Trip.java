package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.transaction.Transaction;

import java.util.Date;
import java.util.List;

public interface Trip {

    long getId();

    String getName();

    String getComment();

    void edit(String name, String comment);

    boolean isActive();

    Date getStartDate();

    Date getEndDate();



    List<Member> getAllMembers();

    List<Member> getActiveMembers();

    Member createMember(String name, int color, int icon);

    Member getMe();

    Member getMemberByName(String name);

    boolean memberIsActive(Member member);

    void setMemberActive(Member member, boolean active);




    List<Transaction> getTransactions();

    void addCost(Member member, Member toMember, String comment, int sum, String image_dir, Date date, boolean isRepayment);

}
