package com.pechenkin.travelmoney.bd.local.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.query.TableRow;

/**
 * Created by pechenkin on 04.04.2018.
 * настройка
 */

public class SettingTableRow extends TableRow {

    public final String name;
    public final String value;
    public SettingTableRow(Cursor c) {
        super(c);
        this.value = getStringColumnValue(Namespace.FIELD_VALUE, c);
        name = getStringColumnValue(Namespace.FIELD_NAME, c);
    }


}
