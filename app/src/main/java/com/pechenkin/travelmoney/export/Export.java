package com.pechenkin.travelmoney.export;

import android.net.Uri;
import android.os.Environment;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

/**
 * Created by pechenkin on 02.04.2018.
 * Функции создания файла для экспорта
 */

public class Export {

    public static void export(TripBaseTableRow pageTrip, ExportFileTypes type) {
        if (pageTrip == null) {
            Help.alert("Не найдена текущая поездка");
            return;
        }
        type.send(pageTrip);
    }







}
