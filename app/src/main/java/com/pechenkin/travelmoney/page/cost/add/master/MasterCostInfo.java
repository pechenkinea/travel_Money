package com.pechenkin.travelmoney.page.cost.add.master;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by pechenkin on 15.05.2018.
 * Информация о трате в мастере
 */

public class MasterCostInfo extends BasePage {

    public static final int ERROR_SUM = 1000000;

    @Override
    public void clickBackButton() {
        setParam();
        PageOpener.INSTANCE.open(MasterWho.class, getParam());
    }

    private Date selectDate = new Date();

    private void setParam() {
        PageParam.BuildingPageParam buildParam = new PageParam.BuildingPageParam(getParam());

        TextInputEditText et_comment = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        buildParam.setName(et_comment.getText().toString());

        TextInputEditText et_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        String sum = et_sum.getText().toString();
        buildParam.setSum(Help.StringToDouble(sum));

        TextView tv_dir = MainActivity.INSTANCE.findViewById(R.id.cost_dir_textView);
        String image_dir = tv_dir.getText().toString();
        buildParam.setPhotoUrl(image_dir);

        buildParam.setSelectDate(selectDate);

        setParam(buildParam.getParam());
    }



    private void commitForm() {
        TextInputEditText et_comment = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        String comment = et_comment.getText().toString();
        if (comment.length() == 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorFillDescription));
            Help.setActiveEditText(R.id.cost_comment);
            return;
        }

        TextInputEditText et_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        String sum = et_sum.getText().toString();
        if (sum.length() == 0 || Help.StringToDouble(sum) <= 0) {
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
        PageOpener.INSTANCE.open(MasterWhom.class, getParam());

    }

    @Override
    public void addEvents() {
        FloatingActionButton commitButton = MainActivity.INSTANCE.findViewById(R.id.cost_info_commit_button);
        commitButton.setOnClickListener(v -> commitForm());

        //Завершение работы с описанием
        TextInputEditText commentText = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        commentText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Help.setActiveEditText(R.id.cost_sum, true);
                return true;
            }
            return false;
        });

        //Кнопка "готово" на сумме
        final TextInputEditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
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
        FloatingActionButton photoButton = MainActivity.INSTANCE.findViewById(R.id.buttonPhoto);
        photoButton.setOnClickListener(v -> {
            Help.hideKeyboard();


            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
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
            MainActivity.INSTANCE.startActivityForResult(intent, MainActivity.TAKE_COST_PHOTO);
        });


        TextInputEditText textDate = MainActivity.INSTANCE.findViewById(R.id.textDate);
        textDate.setOnClickListener(v -> {

            Calendar c = Calendar.getInstance();
            c.setTime(selectDate);


            DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
                Calendar c1 = Calendar.getInstance();

                TimePickerDialog.OnTimeSetListener timeCallBack = (timePicker, hourOfDay, minute) -> {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cal.set(Calendar.MINUTE, minute);

                    ((TextView) MainActivity.INSTANCE.findViewById(R.id.textDate))
                            .setText(Help.dateToDateTimeStr(cal.getTime()));

                    selectDate = cal.getTime();
                };

                TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.INSTANCE, timeCallBack, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), true);

                timeDialog.show();
            };

            DatePickerDialog dateDialog = new DatePickerDialog(MainActivity.INSTANCE, onDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

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

    @Override
    protected boolean fillFields() {

        if (hasParam()) {

            try {
                MemberBaseTableRow member = t_members.getMemberById(getParam().getId());
                if (member != null) {
                    String memberCostInfoText = MainActivity.INSTANCE.getString(R.string.costMember) + " " + member.name;
                    ((TextView) MainActivity.INSTANCE.findViewById(R.id.memberCostInfo))
                            .setText(memberCostInfoText);
                }

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
                        .setText(Help.dateToDateTimeStr(selectDate));

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


}
