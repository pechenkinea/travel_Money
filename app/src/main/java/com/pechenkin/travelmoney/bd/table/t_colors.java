package com.pechenkin.travelmoney.bd.table;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.result.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.result.QueryResultFactory;

public class t_colors {

	static public BaseQueryResult getAll()
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_COLORS;
		return QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);

	}

	static public void resetColors(){


		try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

			db.execSQL("DELETE FROM " + Namespace.TABLE_COLORS + ";");

			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.BLACK + ");");

			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#ff1e1c") + ");");
			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#f9e701") + ");");
			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#0172b6") + ");");

			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#fe8f00") + ");");
			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#008f59") + ");");
			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#7b358e") + ");");

			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor("#ff00ff") + ");");

		}




	}

}
