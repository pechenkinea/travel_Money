package com.pechenkin.travelmoney.page.member;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpenner;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

/**
 * Created by pechenkin on 19.04.2018.
 * Страница добавления нового участника
 */

public class AddMemderPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MembersListPage.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private void formCommit()
    {
        EditText etName =  MainActivity.INSTANCE.findViewById(R.id.editName);

        String name =  etName.getText().toString();
        if ( t_members.isAdded(name) )
        {
            Help.message("Участник с таким именем уже добавлен");
            Help.setActiveEditText(getFocusFieldId());
            return;
        }

        if (name.length() > 0)
        {
            Button buttonColor =  MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
            Drawable background = buttonColor.getBackground();
            int color = 0;
            if (background instanceof ColorDrawable)
                color = ((ColorDrawable) background).getColor();

            long m_id = t_members.add(name, color);
            Help.message("Успешно");
            t_trips.addMemberInTrip(t_trips.ActiveTrip.id, m_id);

            clickBackButton();
        }
        else
        {
            Help.message("Введите имя");
            Help.setActiveEditText(getFocusFieldId());
        }
    }

    @Override
    public void addEvents() {

        Button commitButton = MainActivity.INSTANCE.findViewById(R.id.add_member_commit);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formCommit();
            }
        });

        final EditText nameField = MainActivity.INSTANCE.findViewById(R.id.editName);
        nameField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    formCommit();
                    return true;
                }
                return false;
            }
        });


        nameField.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sum = nameField.getText().toString();
                if (sum.contains(" "))
                    MainActivity.INSTANCE.findViewById(R.id.memberNameWarning).setVisibility(View.VISIBLE);
                else
                    MainActivity.INSTANCE.findViewById(R.id.memberNameWarning).setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Выбор цвета
        Button selectColorButton =  MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
        selectColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(MainActivity.INSTANCE, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                builder.setTitle("Выберите цвет");
                builder.setPreferenceName("ColorPickerDialog");
                //builder.setFlagView(new CustomFlag(this, R.layout.layout_flag));
                builder.setPositiveButton(MainActivity.INSTANCE.getString(R.string.confirm), new ColorListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope colorEnvelope) {
                        Button textView = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
                        textView.setBackgroundColor(colorEnvelope.getColor());
                    }
                });
                builder.setNegativeButton(MainActivity.INSTANCE.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("По умолчанию", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Button textView = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
                        textView.setBackgroundColor(Color.BLACK);
                        dialogInterface.dismiss();
                    }
                });

                builder.getColorPickerView().setPaletteDrawable(MainActivity.INSTANCE.getDrawable(R.drawable.colors));

                builder.show();
            }
        });

    }

    @Override
    protected int getPageId() {
        return R.layout.add_member;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.addNewMember);
    }

    @Override
    protected boolean fillFields() {
        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return  R.id.editName;
    }

    @Override
    protected void helps() {

    }
}
