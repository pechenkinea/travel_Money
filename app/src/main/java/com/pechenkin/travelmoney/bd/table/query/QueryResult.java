package com.pechenkin.travelmoney.bd.table.query;

import android.database.Cursor;

/**
 * Created by pechenkin on 06.04.2018.
 * Запрос к БД
 */

public interface QueryResult {

    BaseTableRow[] getAllRows();
    BaseTableRow getFirstRow();
    boolean hasRows();


    void initializeCountRows(int count);
    void addRow(Cursor c);

}
