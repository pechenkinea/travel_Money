package com.pechenkin.travelmoney.dialog;

import android.app.AlertDialog;
import android.content.Context;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.local.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_colors;
import com.pechenkin.travelmoney.list.AdapterColors;


/**
 * Открывает диалог для выбора цвета
 * цвета берет из базы
 */
public class ColorDialog {

    private ColorDialog() {
    }

    public static void selectColor(Context context, String text, CallBack callBack){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Выберите цвет");

        IdAndNameTableRow[] colors = t_colors.getAll().getAllRows();

        AdapterColors adapterColors = new AdapterColors(MainActivity.INSTANCE, colors, text);

        builder.setAdapter(adapterColors, (dialogInterface, i) -> {
            IdAndNameTableRow selectItem = adapterColors.getItem(i);
            callBack.run((int)selectItem.id);
            dialogInterface.dismiss();

        });

        builder.create().show();
    }


    public interface CallBack{
        void run(int color);
    }

}
