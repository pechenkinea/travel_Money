package com.pechenkin.travelmoney.bd.local.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.local.table.Namespace;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;

import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, Namespace.DB_NAME, null, 24);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_MEMBERS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement,"
                + Namespace.FIELD_NAME + " text,"
                + Namespace.FIELD_COLOR + " integer,"
                + Namespace.FIELD_ICON + " integer,"
                + Namespace.FIELD_ACTIVE + " integer"
                + ");");


        db.execSQL("INSERT INTO " + Namespace.TABLE_MEMBERS + " VALUES (1, 'Я', 0, 0, 1);");


        db.execSQL("create table " + Namespace.TABLE_TRIPS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement,"
                + Namespace.FIELD_UUID + " text,"
                + Namespace.FIELD_NAME + " text,"
                + Namespace.FIELD_PROCESSED + " integer,"
                + Namespace.FIELD_COMMENT + " text"
                + ");");

        db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS + " VALUES (1, '" + UUID.randomUUID().toString() + "','Отпуск', '1', 'Создана по умолчанию');");

        db.execSQL("create table " + Namespace.TABLE_TRIPS_MEMBERS + " ("
                + "trip text not null,"  //TODO надо int
                + "member text not null" //TODO надо int
                + ");");

        db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS_MEMBERS + " VALUES ('1', '1');");


        //createTableCost(db);

        addSettingTable(db);
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.DELETE_COST_SHOWED_HELP + "', '0');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.GROUP_BY_COLOR + "', '0');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.TO_MEMBER_TEXT_LENGTH + "', '12');");

        createTableColors(db);

        createTableTransaction(db);
    }

    private void createTableTransaction(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_TRANSACTION + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement, "
                + Namespace.FIELD_COMMENT + " text, "
                + Namespace.FIELD_IMAGE_DIR + " text, "
                + Namespace.FIELD_ACTIVE + " integer, "
                + Namespace.FIELD_REPAYMENT + " integer, "
                + Namespace.FIELD_TRIP + " integer, "
                + Namespace.FIELD_DATE + " integer, "
                + "FOREIGN KEY(trip) REFERENCES trips(_id)"
                + ");");

        db.execSQL("create table " + Namespace.TABLE_TRANSACTION_ITEMS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement, "
                + Namespace.FIELD_MEMBER + " integer, "
                + Namespace.FIELD_CREDIT + " integer, "
                + Namespace.FIELD_DEBIT + " integer, "
                + Namespace.FIELD_REPAYMENT + " integer, "
                + Namespace.FIELD_TRANSACTION + " integer, "
                + "FOREIGN KEY(" + Namespace.FIELD_MEMBER + ") REFERENCES " + Namespace.TABLE_MEMBERS + "(_id),"
                + "FOREIGN KEY(" + Namespace.FIELD_TRANSACTION + ") REFERENCES " + Namespace.TABLE_TRANSACTION + "(_id)"
                + ");");


    }

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
            db.execSQL("ALTER TABLE " + Namespace.TABLE_MEMBERS + " ADD COLUMN " + Namespace.FIELD_ICON + " integer default 0;");

        }
        // новые цвета
        if (oldVersion < 15) {
            updateColors(db);
        }

        //Добавлен параметр для операций возврата долга
        if (oldVersion < 16) {
            db.execSQL("ALTER TABLE " + Namespace.TABLE_COSTS + " ADD COLUMN " + Namespace.FIELD_REPAYMENT + " integer default 0;");
        }

        //перевод на трпнзакции
        if (oldVersion < 20) {
            createTableTransaction(db);
        }


        //миграция на новую структуру бд и подготовка к работе с удаленной базой
        if (oldVersion < 24) {

            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.NEED_MIGRATION + "', '1');");
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.NEED_ADD_TRIPS_UUID + "', '1');");

            db.execSQL("ALTER TABLE " + Namespace.TABLE_TRIPS + " ADD COLUMN " + Namespace.FIELD_UUID + " text;");
        }


    }
}
