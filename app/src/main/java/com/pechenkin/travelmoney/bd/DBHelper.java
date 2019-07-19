package com.pechenkin.travelmoney.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        super(context, Namespace.DB_NAME, null, 12);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_MEMBERS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement,"
                + Namespace.FIELD_NAME + " text,"
                + Namespace.FIELD_COLOR + " integer,"
                + Namespace.FIELD_ACTIVE + " integer"
                + ");");


        db.execSQL("INSERT INTO " + Namespace.TABLE_MEMBERS + " VALUES (1, 'Я', 0, 1);");


        db.execSQL("create table " + Namespace.TABLE_TRIPS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement,"
                + Namespace.FIELD_NAME + " text,"
                + Namespace.FIELD_PROCESSED + " integer,"
                + Namespace.FIELD_COMMENT + " text"
                + ");");

        db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS + " VALUES (1, 'Отпуск', '1', 'Создана по умолчанию');");

        db.execSQL("create table " + Namespace.TABLE_TRIPS_MEMBERS + " ("
                + "trip text not null,"
                + "member text not null"
                + ");");

        db.execSQL("INSERT INTO " + Namespace.TABLE_TRIPS_MEMBERS + " VALUES ('1', '1');");


        createTableCost(db);

        addSettingTable(db);
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.DELETE_COST_SHOWED_HELP + "', '0');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.GROUP_BY_COLOR + "', '0');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.GROUP_COST + "', '1');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.TO_MEMBER_TEXT_LENGTH + "', '12');");

        createTableColors(db);
    }

    private void createTableCost(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_COSTS + " ("
                + Namespace.FIELD_ID + " integer primary key autoincrement, "
                + "member integer, "
                + "to_member integer, "
                + "comment text, "
                + "sum text, "
                + "image_dir text, "
                + "active integer, "
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

        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.MAIN_PAGE_HELP_ADD_COST_BUTTON + "', '1');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.MAIN_PAGE_HELP_ADD_MEMBERS + "', '1');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.MEMBERS_LIST_HELP + "', '1');");
        db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.TRIPS_LIST_HELP + "', '1');");

    }


    private void createTableColors(SQLiteDatabase db) {

        db.execSQL("create table " + Namespace.TABLE_COLORS + " ("
                + Namespace.FIELD_ID + " integer primary key not null"
                + ");");

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

        //Добавлена настройка для группировки транзакций
        if (oldVersion < 7) {
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.GROUP_COST + "', '1');");
        }

        // Вынесено в настройки кол-во символов учатников в графе "Кому" при включенной группировке
        if (oldVersion < 8) {
            db.execSQL("INSERT INTO " + Namespace.TABLE_SETTINGS + " VALUES ('" + NamespaceSettings.TO_MEMBER_TEXT_LENGTH + "', '12');");
        }

        // Добалена таблица для хранения цветов
        if (oldVersion < 12) {
            createTableColors(db);
        }

    }
}
