package com.pechenkin.travelmoney.bd.local.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;


/**
 * Created by pechenkin on 04.04.2018.
 * Базовый запрос к БД
 */

public class QueryResult<T extends TableRow> {


    //массив а не лист т.к. массивы ковариантны
    private T[] allRows;

    @SuppressWarnings("unchecked")
    public QueryResult(String query, Class<T> tableRowClass) {

        Constructor<T> tableRowConstructor;
        try {
            tableRowConstructor = tableRowClass.getConstructor(Cursor.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Не найден конструктор у tableRowClass", e);
        }

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {
            try (Cursor sqlResult = db.rawQuery(query, null)) {
                if (sqlResult.moveToFirst()) {

                    allRows = (T[]) Array.newInstance(tableRowClass, sqlResult.getCount());
                    int index = 0;

                    do {
                        T row;
                        try {
                            row = tableRowConstructor.newInstance(sqlResult);
                        } catch (Exception e) {
                            throw new RuntimeException("Не удалось создать tableRowClass", e);
                        }
                        allRows[index++] = row;
                    }
                    while (sqlResult.moveToNext());
                } else {
                    allRows = (T[]) Array.newInstance(tableRowClass, 0);
                }
            }
        }
    }


    public T[] getAllRows() {
        return allRows;
    }

    public T getFirstRow() {
        if (hasRows())
            return allRows[0];
        else
            return null;
    }

    public boolean hasRows() {
        return allRows != null && allRows.length > 0;
    }

}
