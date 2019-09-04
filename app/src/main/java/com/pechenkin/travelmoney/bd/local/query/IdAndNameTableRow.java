package com.pechenkin.travelmoney.bd.local.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.table.Namespace;


/**
 * Строка БД, с данными по id и name
 */
public class IdAndNameTableRow extends IdTableRow {

    public final String name;

    protected IdAndNameTableRow(Cursor c) {
        super(c);
        name = getStringColumnValue(Namespace.FIELD_NAME, c);
    }
}
