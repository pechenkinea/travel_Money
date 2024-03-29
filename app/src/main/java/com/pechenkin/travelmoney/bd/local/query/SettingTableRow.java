package com.pechenkin.travelmoney.bd.local.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.table.Namespace;

/**
 * Created by pechenkin on 04.04.2018.
 * настройка
 */

public class SettingTableRow extends TableRow {

    public final String name;
    public final String value;
    public SettingTableRow(Cursor c) {
        super();
        this.value = getStringColumnValue(Namespace.FIELD_VALUE, c);
        name = getStringColumnValue(Namespace.FIELD_NAME, c);
    }


}
