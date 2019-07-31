package com.pechenkin.travelmoney.bd.table.row;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Namespace;

import java.util.Date;

/**
 * Created by pechenkin on 04.04.2018.
 * Строка БД
 */

public class BaseTableRow {

    public final long id;
    public final String name;

    public BaseTableRow(Cursor c)
    {
        id = getLongColumnValue(Namespace.FIELD_ID, c);
        name = getStringColumnValue(Namespace.FIELD_NAME, c);
    }

    String getStringColumnValue(String columnName, Cursor c)
    {
        if (c == null) return "";

        int index = c.getColumnIndex(columnName);
        if (index > -1)
        {
            return c.getString(index);
        }
        return  "";
    }

    long getLongColumnValue(String columnName, Cursor c)
    {
        if (c == null) return -1;

        int index = c.getColumnIndex(columnName);
        if (index > -1)
        {
            return c.getLong(index);
        }
        return  -1;
    }

    int getIntColumnValue(String columnName, Cursor c)
    {
        if (c == null) return -1;

        int index = c.getColumnIndex(columnName);
        if (index > -1)
        {
            return c.getInt(index);
        }
        return  -1;
    }



    double getDoubleColumnValue(String columnName, Cursor c)
    {
        if (c == null) return 0f;

        int index = c.getColumnIndex(columnName);
        if (index > -1)
        {
            return c.getDouble(index);
        }
        return  0f;
    }

    Date getDateColumnValue(String columnName, Cursor c)
    {
        String d = getStringColumnValue(columnName, c);
        try {
            if (d.length() > 0) {
                long val = Long.valueOf(d);
                return new Date(val);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  null;
    }
}
