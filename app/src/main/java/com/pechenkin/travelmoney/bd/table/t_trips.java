package com.pechenkin.travelmoney.bd.table;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.result.BaseQueryResult;
import com.pechenkin.travelmoney.bd.table.result.QueryResultFactory;
import com.pechenkin.travelmoney.bd.table.result.TripsQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class t_trips {

	static public long add(String name, String comment)
    {    	
    	ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_NAME, name);
		cv.put(Namespace.FIELD_COMMENT, comment);
		
		SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();
		long rowID = db.insert(Namespace.TABLE_TRIPS, null, cv);
		db.close();
		return rowID;
    }
	
	static public void edit(long id, String name, String comment)
    {
    	SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();
    	
    	ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_NAME, name);
		cv.put(Namespace.FIELD_COMMENT, comment);
		
        db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_ID + " = " + id, null);
		db.close();
    }
	
	static public Boolean isAdded(String trip_name) {
		String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS +" WHERE " + Namespace.FIELD_NAME + " = '" + trip_name + "'";
		BaseQueryResult result = (BaseQueryResult) QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);
		return result != null && result.hasRows();
	}


	/*
	static public void addTripMembers(long trip_id, ArrayList<Long> l_id)
    {
	 	SQLiteDatabase db = MainActivity.INSTANCE.dbHelper.getWritableDatabase();

    	for (long id : l_id)
    	{
			ContentValues cv = new ContentValues();
    		cv.put(Namespace.FIELD_TRIP, trip_id);
			cv.put(Namespace.FIELD_MEMBER, id);
			db.insert(Namespace.TABLE_TRIPS_MEMBERS, null, cv);
		}
		db.close();
		
    }
	
	static public void editTripMembers(long trip_id, ArrayList<Long> l_id)
    {
	 	SQLiteDatabase db = MainActivity.INSTANCE.dbHelper.getWritableDatabase();
    	
    	db.delete(Namespace.TABLE_TRIPS_MEMBERS, Namespace.FIELD_TRIP + " = " + trip_id, null);
    	
    	for (long id : l_id)
    	{
			ContentValues cv = new ContentValues();
    		cv.put(Namespace.FIELD_TRIP, trip_id);
			cv.put(Namespace.FIELD_MEMBER, id);
			db.insert(Namespace.TABLE_TRIPS_MEMBERS, null, cv);
		}

		db.close();
    }
    */

	static  public  void  removeMemberInTrip(long trip_id, long member_id)
	{
		try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

			db.delete(Namespace.TABLE_TRIPS_MEMBERS, Namespace.FIELD_TRIP + " = " + trip_id + " and " + Namespace.FIELD_MEMBER + " = " + member_id, null);
		}

	}

    static  public  void  addMemberInTrip(long trip_id, long member_id)
	{
		removeMemberInTrip(trip_id, member_id);

		SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_TRIP, trip_id);
		cv.put(Namespace.FIELD_MEMBER, member_id);
		db.insert(Namespace.TABLE_TRIPS_MEMBERS, null, cv);

		db.close();
	}

	static public TripsQueryResult getAll()
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS;
		return (TripsQueryResult)QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);
	}
	
	static public long getIdByName(String t_name)
	{
		String sql = "SELECT " + Namespace.FIELD_ID  + " FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_NAME + " = '" + t_name + "'";

		BaseQueryResult result = (BaseQueryResult)QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);

		BaseTableRow row = result != null ? result.getFirstRow() : null;
		if (row != null)
			return  row.id;

		return -1;
	}

	static public TripBaseTableRow getTripById(long id)
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = " + id ;

		TripsQueryResult result = (TripsQueryResult)QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);

		TripBaseTableRow row = result != null ? result.getFirstRow() : null;
		if (row != null)
			return  row;

		return null;
	}

	static public Boolean isMemberInTrip(long tripId, long memberId)
	{
		if (tripId < 0 || memberId < 0)
			return false;

		String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS_MEMBERS + " WHERE " + Namespace.FIELD_TRIP + " = '" + tripId + "' and " + Namespace.FIELD_MEMBER + " = '" + memberId + "'";
		BaseQueryResult result = (BaseQueryResult) QueryResultFactory.createQueryResult(sql, BaseQueryResult.class);

		return result != null && result.hasRows();
    	
	}
	
	static public void set_active(long t_id)
	{
		SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase();
    	
    	ContentValues cv = new ContentValues();
		cv.put(Namespace.FIELD_PROCESSED, "0");
        db.update(Namespace.TABLE_TRIPS, cv,  Namespace.FIELD_PROCESSED +" != 0", null);
        
        ContentValues cv2 = new ContentValues();
		cv2.put(Namespace.FIELD_PROCESSED, "1");

        db.update(Namespace.TABLE_TRIPS, cv2, Namespace.FIELD_ID + " = " + t_id, null );
		db.close();

		ActiveTrip = getActiveTrip();
	}
	
	static public Boolean isActive(long t_id)
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_ID + " = '" + t_id + "' and "  + Namespace.FIELD_PROCESSED + " = 1";

		TripsQueryResult result = (TripsQueryResult)QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);
		return result != null && result.hasRows();
	}

	static public TripBaseTableRow ActiveTrip;
	static {
		ActiveTrip = getActiveTrip();
	}
	
	private static TripBaseTableRow getActiveTrip()
	{
		String sql = "SELECT * FROM " + Namespace.TABLE_TRIPS + " WHERE " + Namespace.FIELD_PROCESSED + " = '1'";

		TripsQueryResult result = null;
		int tryCount = 0;
		boolean wait = false;

		while ((result == null || !result.hasRows()) && ++tryCount < 5 ) {
			if (wait)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return TripBaseTableRow.getEmpty();
				}
			}
			wait = true;
			result = (TripsQueryResult) QueryResultFactory.createQueryResult(sql, TripsQueryResult.class);
		}

		if (result == null || !result.hasRows())
			return TripBaseTableRow.getEmpty();

		return result.getFirstRow();

	}
		
	
}
