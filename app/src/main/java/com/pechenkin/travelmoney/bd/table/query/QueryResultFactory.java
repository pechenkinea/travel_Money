package com.pechenkin.travelmoney.bd.table.query;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;

/**
 * Created by pechenkin on 06.04.2018.
 * Ферма запросов
 */

public class QueryResultFactory {

   private QueryResultFactory()    {    }

   public static <T extends QueryResult> T createQueryResult(String query, Class<T> typeQuery) {

       T result;

       try {
           result = typeQuery.newInstance();
       } catch (Exception e) {
           e.printStackTrace();
           Help.alert(e.getMessage());
           return null;
       }

       try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getReadableDatabase()) {
           try (Cursor sqlResult = db.rawQuery(query, null)) {
               if (sqlResult.moveToFirst()) {
                   result.initializeCountRows(sqlResult.getCount());
                   do {
                       result.addRow(sqlResult);
                   }
                   while (sqlResult.moveToNext());
               } else {
                   result.initializeCountRows(0);
               }
           }
       }




       return result;
   }
}
