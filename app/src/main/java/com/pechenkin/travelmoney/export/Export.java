package com.pechenkin.travelmoney.export;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.MenuItem;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.result.CostQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.CostBaseTableRow;
import com.pechenkin.travelmoney.Help;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;

/**
 * Created by pechenkin on 02.04.2018.
 * Функции создания файла для экспорта
 */

public class Export {

    public static void export(MenuItem item, ExportFileTypes type, TripBaseTableRow pageTrip) {
        if (pageTrip == null) {
            Help.alert("Не найдена текущая поездка");
            return;
        }

        String fileText = "";

        if (type == ExportFileTypes.CSV) {
            fileText = getCSV(pageTrip);
        } else if (type == ExportFileTypes.JSON) {
            fileText = getJSON(pageTrip);
        }

        if (fileText.length() > 0) {
            toFile(item, fileText, type);
        }
    }

    private static String getCSV(TripBaseTableRow pageTrip) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yy");

        ArrayList<Long> membersList = new ArrayList<>();
        CostQueryResult costs = t_costs.getAllByTripId(pageTrip.id);
        StringBuilder valueCosts = new StringBuilder("Операции\r\n");
        valueCosts.append("Дата;Кто;Кому;Сколько;Активно;Комментарий").append("\r\n");
        if (costs.hasRows()) {
            for (CostBaseTableRow cost : costs.getAllRows()) {
                String line = ((cost.date() != null) ? df2.format(cost.date()) : "") + ";"
                        + cost.member() + ";"
                        + cost.to_member() + ";"
                        + String.valueOf(cost.sum()).replace('.', ',') + ";"
                        + cost.active() + ";"
                        + cost.comment();
                line = line.replaceAll("\n|\n\r", " ");
                valueCosts.append(line).append("\r\n");

                if (!membersList.contains(cost.member()))
                    membersList.add(cost.member());

                if (!membersList.contains(cost.to_member()))
                    membersList.add(cost.to_member());
            }
        }

        StringBuilder valueMembers = new StringBuilder("Участники\r\n");
        valueMembers.append("id;Имя").append("\r\n");
        for (Long m : membersList) {

            BaseTableRow findMemder = t_members.getMemberById(m);
            String line = m + ";" + ((findMemder != null) ? findMemder.name : "ErrorMemderName");
            line = line.replaceAll("\n|\n\r", " ");
            valueMembers.append(line).append("\r\n");

        }
        valueMembers.append("\r\n");
        return valueMembers.append(valueCosts).toString();
    }

    private static String getJSON(TripBaseTableRow pageTrip) {
        JSONObject exportJson = new JSONObject();
        try {
            exportJson.put("tripId", pageTrip.id);
            exportJson.put("tripNmae", pageTrip.name);


            ArrayList<Long> membersList = new ArrayList<>();

            CostQueryResult costs = t_costs.getAllByTripId(pageTrip.id);
            JSONArray json_costs = new JSONArray();
            if (costs.hasRows()) {
                for (CostBaseTableRow cost : costs.getAllRows()) {
                    JSONObject json_cost = new JSONObject();
                    json_cost.put("date", (cost.date() != null) ? cost.date().getTime() : "");
                    json_cost.put("member", cost.member());
                    json_cost.put("to_member", cost.to_member());
                    json_cost.put("sum", cost.sum());
                    json_cost.put("comment", cost.comment());
                    json_cost.put("active", cost.active());
                    json_costs.put(json_cost);

                    if (!membersList.contains(cost.member()))
                        membersList.add(cost.member());

                    if (!membersList.contains(cost.to_member()))
                        membersList.add(cost.to_member());

                }
            }
            exportJson.put("costs", json_costs);


            JSONArray members = new JSONArray();
            for (long m : membersList) {
                JSONObject member = new JSONObject();
                member.put("id", m);
                BaseTableRow findMember = t_members.getMemberById(m);
                member.put("name", (findMember != null) ? findMember.name : "ErrorMemberName");
                members.put(member);
            }

            exportJson.put("members", members);


        } catch (JSONException e) {
            Help.alert("Ошибка формирования json. " + e.getMessage());
            return "";
        }

        return exportJson.toString();
    }

    private static void toFile(MenuItem item, String text, ExportFileTypes type) {
        // формируем объект File, который содержит путь к файлу
        String path = getExportFileFullName();
        if (path.length() == 0)
            return;

        File sdFile = new File(path + "." + type.toString());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write('\ufeff'); // utf-8 BOM
            bw.write(text);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            Help.alert("Ошибка записи: " + e.getMessage());
            return;
        }

        sendFile(item, sdFile, type);
    }

    private static String getExportFileFullName() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Help.alert("SD-карта не доступна: " + Environment.getExternalStorageState());
            return "";
        }

        // получаем путь
        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/travelMoneyExport");
        // создаем каталог
        if (!sdPath.exists() && !sdPath.mkdirs()) {
            Help.alert("Ошибка. Не удалось создать папку для хранения файла. " + sdPath.getAbsolutePath() + MainActivity.INSTANCE.getString(R.string.fileError));
            return "";
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SS");
        return sdPath + "/Export " + dateFormat.format(new Date());
    }


    private static void sendFile(MenuItem item, File sdFile, ExportFileTypes type) {
        ShareActionProvider myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Uri fileUri = FileProvider.getUriForFile(
                MainActivity.INSTANCE,
                MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider", sdFile);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType(type.getMimeTypeName());
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        myShareActionProvider.setShareIntent(sendIntent);

    }
}
