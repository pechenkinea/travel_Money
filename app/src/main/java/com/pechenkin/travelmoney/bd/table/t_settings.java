package com.pechenkin.travelmoney.bd.table;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.result.QueryResultFactory;
import com.pechenkin.travelmoney.bd.table.result.SettingsQueryResult;
import com.pechenkin.travelmoney.bd.table.row.SettingTableRow;
import java.util.HashMap;
import java.util.Map;

public class t_settings {

	private t_settings(){
		getSettings();
	}

    static public t_settings INSTANCE;
	static {
		INSTANCE = new t_settings();
	}

	private Map<String, String> settings = new HashMap<>();
	private void getSettings()
	{
		settings.clear();
		String sql = "SELECT * FROM " + Namespace.TABLE_SETTINGS;
		SettingsQueryResult settingQuery = QueryResultFactory.createQueryResult(sql, SettingsQueryResult.class);
		if (settingQuery != null && settingQuery.hasRows())
		{
			for (SettingTableRow row: settingQuery.getAllRows()) {
				settings.put(row.name, row.getValue());
			}
		}
	}


    public synchronized boolean active(String name)
    {
        if (settings.containsKey(name))
        {
            return settings.get(name).equals("1");
        }
        return false;
    }


    public void revertBoolean(String name)
    {
        try (SQLiteDatabase db = MainActivity.INSTANCE.getDbHelper().getWritableDatabase()) {

            ContentValues cv = new ContentValues();
            cv.put(Namespace.FIELD_NAME, name);
            cv.put(Namespace.FIELD_VALUE, t_settings.INSTANCE.active(name) ? 0 : 1); //Было 0 ставим 1. и наоборот

            try {
                if (settings.containsKey(name)) {
                    db.update(Namespace.TABLE_SETTINGS, cv, Namespace.FIELD_NAME + " = '" + name + "'", null);
                } else {
                    db.insert(Namespace.TABLE_SETTINGS, null, cv);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Help.alert(ex.getMessage());
            }
        }
        getSettings();
    }



	
}
