package com.pechenkin.travelmoney.bd.table.result;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;


/**
 * Created by pechenkin on 04.04.2018.
 * Базовый запрос к БД
 */

public class BaseQueryResult implements QueryResult {

    BaseQueryResult() {
    }

    int index = 0;

    private BaseTableRow[] allRows;

    @Override
    public void initializeCountRows(int count) {
        allRows = new BaseTableRow[count];
    }

    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new BaseTableRow(c);
    }

    @Override
    public BaseTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public BaseTableRow getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    @Override
    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

}
