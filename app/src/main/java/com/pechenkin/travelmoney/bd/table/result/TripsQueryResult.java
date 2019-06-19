package com.pechenkin.travelmoney.bd.table.result;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос поездок
 */

public class TripsQueryResult extends BaseQueryResult {
    TripsQueryResult() {

    }

    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new TripBaseTableRow(c);
    }

    @Override
    public void initializeCountRows(int count) {
        allRows = new TripBaseTableRow[count];
    }

    @Override
    public TripBaseTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public TripBaseTableRow getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    @Override
    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

    private TripBaseTableRow[] allRows;

}
