package com.pechenkin.travelmoney.bd.table.result;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;

/**
 * Created by pechenkin on 06.04.2018.
 * Запрос к БД
 */

interface QueryResult {

    BaseTableRow[] getAllRows();
    BaseTableRow getFirstRow();
    boolean hasRows();


    void initializeCountRows(int count);
    void addRow(Cursor c);




}
