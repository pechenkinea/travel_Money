package com.pechenkin.travelmoney.bd;

import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.security.InvalidParameterException;
import java.util.Date;

public interface Trip {

    String getUUID();

    String getName();

    String getComment();

    void edit(String name, String comment);

    Date getStartDate();

    Date getEndDate();



    StreamList<Member> getAllMembers();

    StreamList<Member> getActiveMembers();

    Member createMember(String name, int color, int icon);

    Member getMe();

    Member getMemberByName(String name);

    Member getMemberById(long id);

    boolean memberIsActive(Member member);

    void setMemberActive(Member member, boolean active);




    StreamList<Transaction> getTransactions();

    Transaction addTransaction(DraftTransaction draftTransaction) throws InvalidParameterException;

}
