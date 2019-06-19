package com.pechenkin.travelmoney.bd.table.row;

import android.database.Cursor;
import com.pechenkin.travelmoney.bd.Namespace;

/**
 * Created by pechenkin on 04.04.2018.
 * настройка
 */

public class SettingTableRow extends BaseTableRow {

    private final String value;
    public SettingTableRow(Cursor c) {
        super(c);
        this.value = getStringColumnValue(Namespace.FIELD_VALUE, c);
    }

    public String getValue() {
        return value;
    }
}
