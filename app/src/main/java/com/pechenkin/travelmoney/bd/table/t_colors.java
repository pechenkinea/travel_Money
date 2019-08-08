package com.pechenkin.travelmoney.bd.table;

import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.query.QueryResultFactory;

public class t_colors {

	static public BaseQueryResult getAll()
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_COLORS;
		return QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);
	}

	/*static public void updateColors(){


		try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

			db.execSQL("DELETE FROM " + Namespace.TABLE_COLORS + ";");

			db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.BLACK + ");");

			String[] colors = MainActivity.INSTANCE.getResources().getStringArray(R.array.member_colors);
			for (String color : colors) {
				db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor(color) + ");");
			}
		}
	}*/

}
