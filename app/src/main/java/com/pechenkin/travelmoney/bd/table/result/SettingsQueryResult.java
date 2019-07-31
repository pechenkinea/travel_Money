package com.pechenkin.travelmoney.bd.table.result;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.row.SettingTableRow;

/**
 * Created by pechenkin on 04.04.2018.
 * Запрос настроек
 */

public class SettingsQueryResult extends BaseQueryResult {
    SettingsQueryResult() {

    }

    @Override
    public void addRow(Cursor c) {
        allRows[index++] = new SettingTableRow(c);
    }

    @Override
    public void initializeCountRows(int count) {
        allRows = new SettingTableRow[count];
    }

    @Override
    public SettingTableRow[] getAllRows() {
        return allRows;
    }

    @Override
    public SettingTableRow getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    @Override
    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

    private SettingTableRow[] allRows;

}
