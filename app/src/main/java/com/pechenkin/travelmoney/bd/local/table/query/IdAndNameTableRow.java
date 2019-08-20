package com.pechenkin.travelmoney.bd.local.table.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.Namespace;


/**
 * Строка БД, с данными по id и name
 */
public class IdAndNameTableRow extends IdTableRow {

    public final String name;

    public IdAndNameTableRow(Cursor c) {
        super(c);
        name = getStringColumnValue(Namespace.FIELD_NAME, c);
    }
}
