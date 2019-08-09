package com.pechenkin.travelmoney.bd.table.query;

/**
 * Created by pechenkin on 06.04.2018.
 * Запрос к БД
 */

public interface QueryResult<T extends TableRow> {

    T[] getAllRows();

    T getFirstRow();

    boolean hasRows();
}
