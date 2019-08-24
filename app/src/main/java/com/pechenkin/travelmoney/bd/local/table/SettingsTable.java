package com.pechenkin.travelmoney.bd.local.table;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.query.QueryResult;
import com.pechenkin.travelmoney.bd.local.query.SettingTableRow;

import java.util.HashMap;
import java.util.Map;

public class SettingsTable {

    private SettingsTable() {
        getSettings();
    }

    static public final SettingsTable INSTANCE;

    static {
        INSTANCE = new SettingsTable();
    }

    private final Map<String, String> settings = new HashMap<>();

    private void getSettings() {
        settings.clear();
        String sql = "SELECT * FROM " + Namespace.TABLE_SETTINGS;
        QueryResult<SettingTableRow> settingQuery = new QueryResult<>(sql, SettingTableRow.class);
        if (settingQuery.hasRows()) {
            for (SettingTableRow row : settingQuery.getAllRows()) {
                settings.put(row.name, row.value);
            }
        }
    }

    public String get(String name, String defaultValue) {
        if (settings.containsKey(name)) {
            return settings.get(name);
        }
        return defaultValue;
    }

    public String get(String name) {
        if (settings.containsKey(name)) {
            return settings.get(name);
        }
        return "";
    }

    public void set(String name, String value) {

        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            ContentValues cv = new ContentValues();
            cv.put(Namespace.FIELD_NAME, name);
            cv.put(Namespace.FIELD_VALUE, value);

            try {
                if (settings.containsKey(name)) {
                    db.update(Namespace.TABLE_SETTINGS, cv, Namespace.FIELD_NAME + " = '" + name + "'", null);
                } else {
                    db.insert(Namespace.TABLE_SETTINGS, null, cv);
                }

                settings.put(name, value);
            } catch (Exception ex) {
                ex.printStackTrace();
                Help.alert(ex.getMessage());
            }
        }
    }

    public synchronized boolean active(String name) {
        if (settings.containsKey(name)) {
            return settings.get(name).equals("1");
        }
        return false;
    }

    public void setActive(String name, boolean active) {
        set(name, active ? "1" : "0");
    }


}
