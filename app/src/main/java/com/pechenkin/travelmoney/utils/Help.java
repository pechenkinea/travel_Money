package com.pechenkin.travelmoney.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Help {


    private Help() {
    }

    //скрыть клавиатуру с экрана
    static public void hideKeyboard() {

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = MainActivity.INSTANCE.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(MainActivity.INSTANCE);
        }
        InputMethodManager imm = (InputMethodManager) MainActivity.INSTANCE.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    static public int dpToPx(int dp) {

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                MainActivity.INSTANCE.getResources().getDisplayMetrics());

    }

    // простое уведомление
    static public void message(String mes) {

        Toast toast = Toast.makeText(MainActivity.INSTANCE.getApplicationContext(), mes, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    static public void alertHelp(String mes) {
        alert(mes, "Подсказка");
    }

    static public void alertError(String mes) {
        alert(mes, "Ошибка");
    }

    //сообщение с кнопкой
    static public void alert(String mes) {
        alert(mes, "");
    }

    private static void alert(String mes, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
        builder.setTitle(title)
                .setMessage(mes)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void setActiveEditText(int fieldId) {
        setActiveEditText(fieldId, false);
    }

    public static void setActiveEditText(int fieldId, boolean selectAll) {
        EditText field = MainActivity.INSTANCE.findViewById(fieldId);
        setActiveEditText(field, selectAll);
    }

    public static void setActiveEditText(EditText field, boolean selectAll) {
        if (field != null) {
            field.requestFocus();
            field.setSelection(field.getText().length());
            InputMethodManager imm = (InputMethodManager) MainActivity.INSTANCE.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
            }

            if (selectAll) {
                field.selectAll();
            }
        }
    }

    /**
     * преобразует текстовое значение рублей в кол-во копеек.
     * Если в дробной части больше 2х знаком то просто отбрасывем их.
     *
     * @param value 123 456,54
     * @return 12345654
     */
    public static int textRubToIntKop(String value) {
        if (value == null || value.length() == 0)
            return 0;

        double val = StringToDouble(value);
        return (int)(val * 100);
    }

    /**
     * Показывает пользователю число копеек в рублях
     * @param kop 12345
     * @return 123.45
     */
    public static String kopToTextRub(int kop){
        return doubleToString((double) kop / 100);
    }


    public static double StringToDouble(String value) {
        if (value == null || value.length() == 0)
            return 0;
        try {
            return Double.valueOf(value.replaceAll(" ", ""));
        } catch (Exception ex) {
            return 0;
        }
    }


    public static String doubleToString(double value) {
        if (value == 0)
            return "0";

        String result = doubleFormat.format(value);
        if (result.endsWith(".00")) {
            result = result.replace(".00", "");
        }
        if (result.startsWith(".")) {
            result = "0" + result;
        }

        // если больше 100_000 то копейки скрываем, что бы не получались сильно длинные цифры
        if (value >= 100_000) {
            result = result.replaceAll("\\..*", "");
        }

        return result;
    }

    private static final DecimalFormat doubleFormat;

    static {
        doubleFormat = new DecimalFormat("#.00");
        doubleFormat.setGroupingUsed(true);
        doubleFormat.setGroupingSize(3);


        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(' ');

        doubleFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }


    public static int getBackgroundColor(View view) {
        Drawable background = view.getBackground();
        int color = 0;
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();

        return color;
    }


    private static final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(MainActivity.INSTANCE);
    private static final DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(MainActivity.INSTANCE);

    public static String dateToDateTimeStr(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat.format(date) + " " + timeFormat.format(date);
    }

    public static String dateToDateStr(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat.format(date);
    }

    public static void showFabWithAnimation(FloatingActionButton fab) {

        AlphaAnimation animation1 = new AlphaAnimation(0, 0.85F);
        animation1.setDuration(700);
        animation1.setFillAfter(true);
        fab.setAnimation(animation1);
        ((View) fab).setVisibility(View.VISIBLE);
    }


    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException ignored) {

        }
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog);
        // dialog.setMessage(Message);
        return dialog;
    }


}
