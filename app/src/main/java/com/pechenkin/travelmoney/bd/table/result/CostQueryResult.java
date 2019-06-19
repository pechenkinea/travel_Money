package com.pechenkin.travelmoney.bd.table.result;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.row.CostBaseTableRow;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос трат
 */

public class CostQueryResult extends BaseQueryResult {
    CostQueryResult() {
    }

    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new CostBaseTableRow(c);
    }

    private CostBaseTableRow[] allRows;

    @Override
    public void initializeCountRows(int count) {
        allRows = new CostBaseTableRow[count];
    }

    @Override
    public CostBaseTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public CostBaseTableRow getFirstRow() {
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
