package com.pechenkin.travelmoney.page.member;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.dialog.ColorDialog;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPageNew;


/**
 * Created by pechenkin on 19.04.2018.
 * Страница добавления нового участника
 */

public class AddMemderPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPageNew.class, new PageParam.BuildingPageParam().setId(R.id.navigation_members).getParam());
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

        FloatingActionButton commitButton = MainActivity.INSTANCE.findViewById(R.id.add_member_commit);
        commitButton.setOnClickListener(v -> formCommit());

        final EditText nameField = MainActivity.INSTANCE.findViewById(R.id.editName);
        nameField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                formCommit();
                return true;
            }
            return false;
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
        selectColorButton.setOnClickListener(v -> {

            String text = nameField.getText().toString();
            if (text.length() == 0){
                text = "Имя не задано";
            }

            ColorDialog.selectColor(MainActivity.INSTANCE, text, selectColorButton::setBackgroundColor);
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


}
