package com.pechenkin.travelmoney.bd.table.query;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Namespace;

import java.util.Date;


/**
 * Пустая строка, работает быстрее всех т.к. не получает значение не из каких ячеек
 */
public class TableRow {


    public TableRow(Cursor c) {

    }


    protected String getStringColumnValue(String columnName, Cursor c) {
        if (c == null) return "";

        int index = c.getColumnIndex(columnName);
        if (index > -1) {
            return c.getString(index);
        }
        return "";
    }

    protected long getLongColumnValue(String columnName, Cursor c) {
        if (c == null) return -1;

        int index = c.getColumnIndex(columnName);
        if (index > -1) {
            return c.getLong(index);
        }
        return -1;
    }

    protected int getIntColumnValue(String columnName, Cursor c) {
        if (c == null) return -1;

        int index = c.getColumnIndex(columnName);
        if (index > -1) {
            return c.getInt(index);
        }
        return -1;
    }


    protected double getDoubleColumnValue(String columnName, Cursor c) {
        if (c == null) return 0f;

        int index = c.getColumnIndex(columnName);
        if (index > -1) {
            return c.getDouble(index);
        }
        return 0f;
    }

    protected Date getDateColumnValue(String columnName, Cursor c) {
        String d = getStringColumnValue(columnName, c);
        try {
            if (d.length() > 0) {
                long val = Long.valueOf(d);
                return new Date(val);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
