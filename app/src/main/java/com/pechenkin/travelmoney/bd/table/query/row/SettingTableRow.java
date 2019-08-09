package com.pechenkin.travelmoney.bd.table.query.row;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.TableRow;

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
