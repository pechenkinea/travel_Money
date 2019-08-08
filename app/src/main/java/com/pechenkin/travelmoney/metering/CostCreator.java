package com.pechenkin.travelmoney.metering;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.query.member.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.query.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;

import java.util.Date;
import java.util.Random;

class CostCreator {

    /*
     * Добавляет в текущую поездку 4000 операций разом.
     * каждые 4 имеют общую дату и комментарий для группировки
     */
    public static void createBigCostList() {

        final ProgressDialog progressDialog = Help.createProgressDialog(MainActivity.INSTANCE);
        progressDialog.show();

        Thread printCostThready = new Thread(() -> {

            MembersQueryResult currentTripMembers = t_members.getAllByTripId(t_trips.ActiveTrip.id);
            BaseTableRow[] members = currentTripMembers.getAllRows();

            try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {


                long date = new Date().getTime();

                Random memberRandom = new Random();

                for (int i = 0; i < 1000; i++) {

                    BaseTableRow member = members[memberRandom.nextInt(members.length)];
                    String comment = "comment " + i;

                    String dateStr = String.valueOf(date + i);

                    for (int g = 0; g < 4; g++) {

                        ContentValues cv = new ContentValues();
                        cv.put(Namespace.FIELD_MEMBER, member.id);
                        cv.put(Namespace.FIELD_TO_MEMBER, members[memberRandom.nextInt(members.length)].id);
                        cv.put(Namespace.FIELD_COMMENT, comment);
                        cv.put(Namespace.FIELD_SUM, String.valueOf(new Random().nextInt(300)));
                        cv.put(Namespace.FIELD_IMAGE_DIR, "");
                        cv.put(Namespace.FIELD_ACTIVE, 1);
                        cv.put(Namespace.FIELD_TRIP, t_trips.ActiveTrip.id);
                        cv.put(Namespace.FIELD_DATE, dateStr);

                        db.insert(Namespace.TABLE_COSTS, null, cv);
                    }

                }
            }
            progressDialog.dismiss();
        });

        printCostThready.start();


    }
}
