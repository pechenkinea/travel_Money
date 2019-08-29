package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;

import java.security.InvalidParameterException;
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

    Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException;

}
