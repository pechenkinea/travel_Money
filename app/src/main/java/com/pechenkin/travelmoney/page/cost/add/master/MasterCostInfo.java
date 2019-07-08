package com.pechenkin.travelmoney.page.cost.add.master;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpenner;
import com.pechenkin.travelmoney.page.PageParam;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pechenkin on 15.05.2018.
 * Информация о трате в мастере
 */

public class MasterCostInfo extends BasePage {

    public static int ERROR_SUM = 1000000;

    @Override
    public void clickBackButton() {
        setParam();
        PageOpenner.INSTANCE.open(MasterWho.class, getParam());
    }

    private Date selectDate = new Date();

    private void setParam() {
        PageParam.BuildingPageParam buildParam = new PageParam.BuildingPageParam(getParam());

        EditText et_comment = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        buildParam.setName(et_comment.getText().toString());

        EditText et_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        String sum = et_sum.getText().toString();
        buildParam.setSum(Help.StringToDouble(sum));

        TextView tv_dir = MainActivity.INSTANCE.findViewById(R.id.cost_dir_textView);
        String image_dir = tv_dir.getText().toString();
        buildParam.setFotoUrl(image_dir);

        buildParam.setSelectDate(selectDate);

        setParam(buildParam.getParam());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private void commitForm() {
        EditText et_comment = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        String comment = et_comment.getText().toString();
        if (comment.length() == 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorFillDescription));
            Help.setActiveEditText(R.id.cost_comment);
            return;
        }

        EditText et_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        String sum = et_sum.getText().toString();
        if (sum.length() == 0 ||  Help.StringToDouble(sum) <= 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorFillSum));
            Help.setActiveEditText(R.id.cost_sum, true);
            return;
        }


        if (Help.StringToDouble(sum) > ERROR_SUM) {
            Help.message(String.format(MainActivity.INSTANCE.getString(R.string.errorBigSum) + "", Help.DoubleToString(ERROR_SUM)));
            Help.setActiveEditText(R.id.cost_sum);
            return;
        }

        setParam();
        PageOpenner.INSTANCE.open(MasterWhom.class, getParam());

    }

    @Override
    public void addEvents() {
        Button commitButton = MainActivity.INSTANCE.findViewById(R.id.cost_info_commit_button);
        commitButton.setOnClickListener(v -> commitForm());

        //Завершение работы с описанием
        EditText commentText = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        commentText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Help.setActiveEditText(R.id.cost_sum, true);
                return true;
            }
            return false;
        });

        //Кнопка "готово" на сумме
        final EditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        cost_sum.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                commitForm();
                return true;
            }
            return false;
        });


        cost_sum.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sum = cost_sum.getText().toString();
                if (Help.StringToDouble(sum) > ERROR_SUM) {
                    cost_sum.setText(String.valueOf(ERROR_SUM));
                    cost_sum.setSelection(cost_sum.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Кнопка для фотографии
        ImageButton fotoButton = MainActivity.INSTANCE.findViewById(R.id.buttonFoto);
        fotoButton.setOnClickListener(v -> {
            Help.hideKeyboard();


            if (!Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Help.alert("SD-карта не доступна: " + Environment.getExternalStorageState());
                return;
            }

            Date now = new Date();
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            File folderForPhoto = new File(path.getAbsolutePath() + "/travel_Money");
            if (!folderForPhoto.exists() && !folderForPhoto.mkdirs()) {
                Help.alert("Ошибка. Не удалось создать папку для хранения фото. " + folderForPhoto.getAbsolutePath() + MainActivity.INSTANCE.getString(R.string.fileError));
                return;
            }

            File file = new File(folderForPhoto.getAbsolutePath(), now.getTime() + ".jpg");


            //Создаем uri, через который будет доступен файл для intent камеры
            Uri outputFileUri = FileProvider.getUriForFile(
                    MainActivity.INSTANCE,
                    MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider", file);

            if (outputFileUri == null) {
                Help.alert("Ошибка. Не удалось создать файл для фото.");
                return;
            }

            MainActivity.INSTANCE.photoFileUri = file.getAbsolutePath(); // Значение, для хранения в базе

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            MainActivity.INSTANCE.setResult(RESULT_OK, intent);
            MainActivity.INSTANCE.startActivityForResult(intent, MainActivity.TAKE_COST_FOTO);
        });


        EditText textDate = MainActivity.INSTANCE.findViewById(R.id.textDate);
        textDate.setOnClickListener(v -> {

            Calendar c = Calendar.getInstance();
            c.setTime(selectDate);

            DatePickerDialog dateDialog = new DatePickerDialog(MainActivity.INSTANCE, (view, year, monthOfYear, dayOfMonth) -> {

                Calendar c1 = Calendar.getInstance();

                TimePickerDialog timeDialog = TimePickerDialog.newInstance((view1, hourOfDay, minute, second) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

                    ((TextView) MainActivity.INSTANCE.findViewById(R.id.textDate))
                            .setText(dateFormat.format(cal.getTime()));

                    selectDate = cal.getTime();
                }, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), true);

                if (c1.get(Calendar.YEAR) == year && c1.get(Calendar.DAY_OF_MONTH) == dayOfMonth && c1.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                    timeDialog.setMaxTime(c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), c1.get(Calendar.SECOND));
                }

                timeDialog.show(MainActivity.INSTANCE.getSupportFragmentManager(), "");
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


            Calendar cMax = Calendar.getInstance();
            cMax.set(Calendar.HOUR_OF_DAY, 23);
            cMax.set(Calendar.MINUTE, 59);


            dateDialog.getDatePicker().setMaxDate(cMax.getTime().getTime());

            dateDialog.show();
        });


    }

    @Override
    protected int getPageId() {
        return R.layout.master_cost_info;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.costInfo);
    }

    @SuppressLint("SimpleDateFormat")
    static private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm");

    @SuppressLint("SetTextI18n")
    @Override
    protected boolean fillFields() {

        if (hasParam()) {

            try {
                ((TextView) MainActivity.INSTANCE.findViewById(R.id.memberCostInfo))
                        .setText(MainActivity.INSTANCE.getString(R.string.costMember) + " " + t_members.getMemberById(getParam().getId()).name);

                ((EditText) MainActivity.INSTANCE.findViewById(R.id.cost_comment))
                        .setText(getParam().getName());

                ((EditText) MainActivity.INSTANCE.findViewById(R.id.cost_sum))
                        .setText(Help.DoubleToString(getParam().getSum()).replaceAll(" ", ""));

                if (getParam().getFotoUrl().length() > 0) {
                    ((TextView) MainActivity.INSTANCE.findViewById(R.id.cost_dir_textView))
                            .setText(getParam().getFotoUrl());

                    MainActivity.INSTANCE.findViewById(R.id.hasPhoto).setVisibility(View.VISIBLE);
                }


                selectDate = getParam().getSelectDate();
                ((TextView) MainActivity.INSTANCE.findViewById(R.id.textDate))
                        .setText(dateFormat.format(selectDate));

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return R.id.cost_comment;
    }

    @Override
    protected void helps() {

    }
}
