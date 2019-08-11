package com.pechenkin.travelmoney.bd.table.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Namespace;


/**
 * Строка БД, с данными только по id
 */
public class IdTableRow extends TableRow {

    public final long id;

    public IdTableRow(Cursor c) {
        super(c);
        id = getLongColumnValue(Namespace.FIELD_ID, c);
    }
}
