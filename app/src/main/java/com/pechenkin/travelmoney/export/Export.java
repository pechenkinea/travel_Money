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

        String fileText = type.getText(pageTrip);
        if (fileText.length() == 0) {
            Help.alertError("Не удалось подготовить текст файла для экспорта");
            return;
        }

        File exportFile = getFile(fileText, type);
        if (exportFile != null) {
            sendFile(exportFile, type);
        }
    }

    private static File getFile(String text, ExportFileTypes type) {
        // формируем объект File, который содержит путь к файлу
        String path = getExportFileFullName();
        if (path.length() == 0)
            return null;

        File sdFile = new File(path + "." + type.toString());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write('\ufeff'); // utf-8 BOM
            bw.write(text);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            Help.alertError("Ошибка записи: " + e.getMessage());
            return null;
        }
        return sdFile;

    }

    private static String getExportFileFullName() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Help.alert("SD-карта не доступна: " + Environment.getExternalStorageState());
            return "";
        }

        // получаем путь
        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/travelMoneyExport");
        // создаем каталог
        if (!sdPath.exists() && !sdPath.mkdirs()) {
            Help.alertError("Не удалось создать папку для хранения файла. " + sdPath.getAbsolutePath() + MainActivity.INSTANCE.getString(R.string.fileError));
            return "";
        }

        return sdPath + "/Export " + new Date().getTime();
    }


    private static void sendFile(File sdFile, ExportFileTypes type) {

        Uri fileUri = FileProvider.getUriForFile(
                MainActivity.INSTANCE,
                MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider", sdFile);


        ShareCompat.IntentBuilder.from(MainActivity.INSTANCE)
                .setType(type.getMimeTypeName())
                .setStream(fileUri)
                .startChooser();

    }
}
