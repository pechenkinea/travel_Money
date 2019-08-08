package com.pechenkin.travelmoney.bd.table.query.member;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос участников
 */

public class MembersQueryResult extends BaseQueryResult {


    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new MemberTableRow(c);
    }

    @Override
    public void initializeCountRows(int count) {
        allRows = new MemberTableRow[count];
    }

    @Override
    public MemberTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public MemberTableRow getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    @Override
    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

    private MemberTableRow[] allRows;

}
