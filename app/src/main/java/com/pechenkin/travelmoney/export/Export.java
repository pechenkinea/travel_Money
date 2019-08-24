package com.pechenkin.travelmoney.export;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.bd.Trip;

/**
 * Created by pechenkin on 02.04.2018.
 * Функции создания файла для экспорта
 */

public class Export {

    public static void export(Trip pageTrip, ExportFileTypes type) {
        if (pageTrip == null) {
            Help.alert("Не найдена текущая поездка");
            return;
        }
        type.send(pageTrip);
    }







}
