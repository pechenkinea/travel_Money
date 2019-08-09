package com.pechenkin.travelmoney.export.formats.send.types;

import android.net.Uri;
import android.os.Environment;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.row.TripTableRow;
import com.pechenkin.travelmoney.export.formats.ExportFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

public class TextFile implements SendType {


    @Override
    public void send(TripTableRow pageTrip, ExportFormat exportFormat) {
        String text = exportFormat.getText(pageTrip);

        File exportFile = getFile(text, exportFormat);
        if (exportFile != null) {
            sendFile(exportFile, exportFormat);
        }
    }


    private File getFile(String text, ExportFormat exportFormat) {
        // формируем объект File, который содержит путь к файлу
        String path = getExportFileFullName();
        if (path.length() == 0)
            return null;

        File sdFile = new File(path + "." + exportFormat.getExpansionType());
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


    private String getExportFileFullName() {
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

    private static void sendFile(File sdFile, ExportFormat exportFormat) {

        Uri fileUri = FileProvider.getUriForFile(
                MainActivity.INSTANCE,
                MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider", sdFile);


        ShareCompat.IntentBuilder.from(MainActivity.INSTANCE)
                .setType(exportFormat.getMimeType())
                .setStream(fileUri)
                .startChooser();

    }


}
