package com.pechenkin.travelmoney.bd.table.query.trip;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос поездок
 */

public class TripsQueryResult extends BaseQueryResult {

    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new TripTableRow(c);
    }

    @Override
    public void initializeCountRows(int count) {
        allRows = new TripTableRow[count];
    }

    @Override
    public TripTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public TripTableRow getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    @Override
    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

    private TripTableRow[] allRows;

}
