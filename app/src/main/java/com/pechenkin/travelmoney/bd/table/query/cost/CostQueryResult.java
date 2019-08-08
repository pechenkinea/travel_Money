package com.pechenkin.travelmoney.bd.table.query.cost;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос трат
 */

public class CostQueryResult extends BaseQueryResult {


    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new CostTableRow(c);
    }

    private CostTableRow[] allRows;

    @Override
    public void initializeCountRows(int count) {
        allRows = new CostTableRow[count];
    }

    @Override
    public CostTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public CostTableRow getFirstRow() {
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
