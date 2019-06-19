package com.pechenkin.travelmoney.bd.table;

import java.util.Date;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.result.QueryResultFactory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class t_costs {

	private  t_costs(){}
	
	static public long add(long member_id, long to_member_id, String comment, double sum, String image_dir, long tripId, Date date)
    {
		ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_MEMBER, member_id);
		cv.put(Namespace.FIELD_TO_MEMBER, to_member_id);
		cv.put(Namespace.FIELD_COMMENT, comment);
		cv.put(Namespace.FIELD_SUM, sum);
		cv.put(Namespace.FIELD_IMAGE_DIR, image_dir);
		cv.put(Namespace.FIELD_ACTIVE, 1);
		cv.put(Namespace.FIELD_TRIP, tripId);
		cv.put(Namespace.FIELD_DATE, date.getTime());
		
				
		SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();
		long rowID = db.insert(Namespace.TABLE_COSTS, null, cv);
		db.close();
		return rowID;
    }
	
	static public CostQueryResult getAllByTripId(long t_id)
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_COSTS + " where " + Namespace.FIELD_TRIP + " = '" + t_id + "' ORDER BY " + Namespace.FIELD_DATE + " DESC";
		return QueryResultFactory.createQueryResult(sql, CostQueryResult.class);
	}

	static public void disable_cost(long id)
	{		
		SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_ACTIVE, 0);
		
        db.update(Namespace.TABLE_COSTS, cv, Namespace.FIELD_ID + " = " + id, null);
        db.close();
	}
	
	static public void enable_cost(long id)
	{		
		SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_ACTIVE, 1);
		
        db.update(Namespace.TABLE_COSTS, cv, Namespace.FIELD_ID + " = " + id, null);
        db.close();
	}


}
