package com.pechenkin.travelmoney.bd.table.result;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос участников
 */

public class MembersQueryResult extends BaseQueryResult {
    MembersQueryResult() {

    }

    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new MemberBaseTableRow(c);
    }

    @Override
    public void initializeCountRows(int count) {
        allRows = new MemberBaseTableRow[count];
    }

    @Override
    public MemberBaseTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public MemberBaseTableRow getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    @Override
    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

    private MemberBaseTableRow[] allRows;

}
