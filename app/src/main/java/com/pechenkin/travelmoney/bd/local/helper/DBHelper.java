package com.pechenkin.travelmoney.bd.local.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.TripStore;
import com.pechenkin.travelmoney.bd.local.helper.update.ToUUID;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;

import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, Namespace.DB_NAME, null, 29);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_MEMBERS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement,"
                + Namespace.FIELD_UUID + " text,"
                + Namespace.FIELD_TRIP_UUID + " text,"
                + Namespace.FIELD_NAME + " text,"
                + Namespace.FIELD_COLOR + " integer,"
                + Namespace.FIELD_ICON + " integer,"
                + Namespace.FIELD_ACTIVE + " integer"
                + ");");


        String firstMemberUuid = UUID.randomUUID().toString();
        db.execSQL("INSERT INTO " + Namespace.TABLE_MEMBERS + " VALUES (1, '" + firstMemberUuid + "', '', 'Я', 0, 0, 1);");


        db.execSQL("create table " + Namespace.TABLE_TRIPS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement,"
                + Namespace.FIELD_UUID + " text,"
                + Namespace.FIELD_NAME + " text,"
                + Namespace.FIELD_PROCESSED + " integer,"
                + Namespace.FIELD_COMMENT + " text,"
                + Namespace.FIELD_STORE + " text"
                + ");");

        String firstTripUuid = UUID.randomUUID().toString();
        db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS + " VALUES (1, '" + firstTripUuid + "','Отпуск', '1', 'Создана по умолчанию', '" + TripStore.LOCAL + "');");

        createTableTripsMembers(db);
        db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS_MEMBERS + " VALUES ('" + firstTripUuid + "', '" + firstMemberUuid + "');");


        addSettingTable(db);
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.DELETE_COST_SHOWED_HELP + "', '0');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.GROUP_BY_COLOR + "', '0');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.TO_MEMBER_TEXT_LENGTH + "', '12');");

        createTableColors(db);

        createTableTransaction(db);
    }

    public static void createTableTripsMembers(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_TRIPS_MEMBERS + " ("
                + Namespace.FIELD_TRIP_UUID + " text,"
                + Namespace.FIELD_MEMBER_UUID + " text"
                + ");");
    }

    private void createTableTransaction(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_TRANSACTION + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement, "
                + Namespace.FIELD_UUID + " text,"
                + Namespace.FIELD_COMMENT + " text, "
                + Namespace.FIELD_IMAGE_DIR + " text, "
                + Namespace.FIELD_ACTIVE + " integer, "
                + Namespace.FIELD_REPAYMENT + " integer, "
                + Namespace.FIELD_TRIP_UUID + " text, "
                + Namespace.FIELD_DATE + " integer"
                + ");");

        db.execSQL("create table " + Namespace.TABLE_TRANSACTION_ITEMS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement, "
                + Namespace.FIELD_UUID + " text,"
                + Namespace.FIELD_MEMBER_UUID + " text, "
                + Namespace.FIELD_CREDIT + " integer, "
                + Namespace.FIELD_DEBIT + " integer, "
                + Namespace.FIELD_TRANSACTION_UUID + " text"
                + ");");


    }

    @Deprecated
    private void createTableCost(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_COSTS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement, "
                + "member integer, "
                + "to_member integer, "
                + "comment text, "
                + "sum text, "
                + Namespace.FIELD_SUM + " integer, "
                + "image_dir text, "
                + "active integer, "
                + Namespace.FIELD_REPAYMENT + " integer, "
                + "trip integer, "
                + "date integer, "
                + "FOREIGN KEY(member) REFERENCES members(_id),"
                + "FOREIGN KEY(to_member) REFERENCES members(_id),"
                + "FOREIGN KEY(trip) REFERENCES trips(_id)"
                + ");");
    }

    private void addSettingTable(SQLiteDatabase db) {
        db.execSQL("create table " + Namespace.TABLE_SETTINGS + " ("
                + Namespace.FIELD_NAME + " text primary key not null,"
                + Namespace.FIELD_VALUE + " text not null"
                + ");");
    }


    private void createTableColors(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_COLORS + " ("
                + Namespace.FIELD_ID + " integer primary key not null"
                + ");");

        updateColors(db);

    }

    private void updateColors(SQLiteDatabase db) {

        db.execSQL("DELETE FROM " + Namespace.TABLE_COLORS + ";");

        db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.BLACK + ");");

        String[] colors = MainActivity.INSTANCE.getResources().getStringArray(R.array.member_colors);
        for (String color : colors) {
            db.execSQL("INSERT INTO " + Namespace.TABLE_COLORS + " VALUES (" + Color.parseColor(color) + ");");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Добавлена возможность указать цвет для участника
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + Namespace.TABLE_MEMBERS + " ADD COLUMN " + Namespace.FIELD_COLOR + " integer;");
        }

        //Добавлена таблица для хранения настроек
        if (oldVersion < 3) {
            addSettingTable(db);
        }

        //Добавлена настройка DELETE_COST_SHOWED_HELP
        if (oldVersion < 4) {
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.DELETE_COST_SHOWED_HELP + "', '0');");
        }

        //Обновлена структура таблицы costs
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE " + Namespace.TABLE_COSTS + " RENAME TO tmp_table_name");
            createTableCost(db);
            db.execSQL("INSERT INTO " + Namespace.TABLE_COSTS + " SELECT * FROM tmp_table_name;");
            db.execSQL("DROP TABLE tmp_table_name;");
        }


        //Добавлена настройка для группировки по цветам
        if (oldVersion < 6) {
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.GROUP_BY_COLOR + "', '0');");
        }


        // Вынесено в настройки кол-во символов учатников в графе "Кому" при включенной группировке
        if (oldVersion < 8) {
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.TO_MEMBER_TEXT_LENGTH + "', '12');");
        }

        // Добалена таблица для хранения цветов
        if (oldVersion < 12) {
            createTableColors(db);
        }

        // Добавлены миниатюры для участников
        if (oldVersion < 13) {
            addColumn(db, Namespace.TABLE_MEMBERS, Namespace.FIELD_ICON, "integer", "0");

        }
        // новые цвета
        if (oldVersion < 15) {
            updateColors(db);
        }

        //Добавлен параметр для операций возврата долга
        if (oldVersion < 16) {
            addColumn(db, Namespace.TABLE_COSTS, Namespace.FIELD_REPAYMENT, "integer", "0");
        }

        //перевод на трпнзакции
        if (oldVersion < 20) {
            createTableTransaction(db);
        }


        //миграция на новую структуру бд и подготовка к работе с удаленной базой
        if (oldVersion < 24) {

            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.NEED_MIGRATION + "', '1');");
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.NEED_ADD_TRIPS_UUID + "', '1');");

            addColumn(db, Namespace.TABLE_TRIPS, Namespace.FIELD_UUID, "text", "''");
        }

        //Исправление типа в столюцах таблицы TABLE_TRIPS_MEMBERS
        if (oldVersion < 25) {
            db.execSQL("ALTER TABLE " + Namespace.TABLE_TRIPS_MEMBERS + " RENAME TO tmp_table_name");
            createTableTripsMembers(db);
            db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS_MEMBERS + " SELECT * FROM tmp_table_name;");
            db.execSQL("DROP TABLE tmp_table_name;");
        }


        //Исправление ошибки после обновления
        if (oldVersion < 26) {
            db.execSQL("DELETE FROM " + Namespace.TABLE_TRANSACTION + ";");
            db.execSQL("DELETE FROM " + Namespace.TABLE_TRANSACTION_ITEMS + ";");
            db.execSQL("UPDATE " + Namespace.TABLE_SETTINGS +
                    " SET " + Namespace.FIELD_VALUE + " = '1'" +
                    " WHERE " + Namespace.FIELD_NAME + " = '" + NamespaceSettings.NEED_MIGRATION + "';"
            );
        }

        //Отметка о том где лежат данные по поездке
        if (oldVersion < 27) {
            addColumn(db, Namespace.TABLE_TRIPS, Namespace.FIELD_STORE, "text", "'" + TripStore.LOCAL.toString() + "'");
        }

        //Добавление поля uuid
        if (oldVersion < 28) {
            addColumn(db, Namespace.TABLE_MEMBERS, Namespace.FIELD_UUID, "text", "''");
            addColumn(db, Namespace.TABLE_TRANSACTION, Namespace.FIELD_UUID, "text", "''");
            addColumn(db, Namespace.TABLE_TRANSACTION_ITEMS, Namespace.FIELD_UUID, "text", "''");

        }
        // перевод id на uuid
        if (oldVersion < 29) {
            addColumn(db, Namespace.TABLE_TRANSACTION_ITEMS, Namespace.FIELD_MEMBER_UUID, "text", "''");
            addColumn(db, Namespace.TABLE_TRANSACTION_ITEMS, Namespace.FIELD_TRANSACTION_UUID, "text", "''");

            addColumn(db, Namespace.TABLE_MEMBERS, Namespace.FIELD_TRIP_UUID, "text", "''");
            addColumn(db, Namespace.TABLE_TRANSACTION, Namespace.FIELD_TRIP_UUID, "text", "''");

            ToUUID.execute(db);
        }


    }

    private void addColumn(SQLiteDatabase db, String tableName, String columnName, String type, String defaultValue) {
        try {
            db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + type + " default " + defaultValue + ";");
        } catch (SQLiteException ex) {
            Log.w("DBHelper", "Altering " + tableName + ": " + ex.getMessage());
        }

    }
}
