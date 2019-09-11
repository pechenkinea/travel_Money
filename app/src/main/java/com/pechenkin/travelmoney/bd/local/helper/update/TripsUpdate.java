package com.pechenkin.travelmoney.bd.local.helper.update;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;

import java.util.UUID;

public class TripsUpdate {

    public static void updateUUID() {

        TripTableRow[] trips = TableTrip.INSTANCE.getAll();
        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            for (TripTableRow trip : trips) {
                ContentValues cv = new ContentValues();
                cv.put(Namespace.FIELD_UUID, UUID.randomUUID().toString());
                db.update(Namespace.TABLE_TRIPS, cv, Namespace.FIELD_ID + " = " + trip.id, null);

            }

        }
    }
}
