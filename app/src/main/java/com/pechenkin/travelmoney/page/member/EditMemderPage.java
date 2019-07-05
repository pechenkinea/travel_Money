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

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.dialog.ColorDialog;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpenner;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница редактирования участника
 * Нужно обязательно передать в параметры id сотрудника
 */

public class EditMemderPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MembersListPage.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    private void formCommit() {
        EditText etName = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
        String name = etName.getText().toString();

        if (name.length() < 1) {
            Help.message("Введите имя");
            Help.setActiveEditText(getFocusFieldId());
            return;
        }

        long findId = t_members.getIdByName(etName.getText().toString());
        if (findId >= 0 && findId != getParam().getId()) {
            Help.message("Имя занято другим участником");
            Help.setActiveEditText(getFocusFieldId());
            return;
        }


        Button buttonColor = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
        int color = Help.getBackgroundColor(buttonColor);

        t_members.edit(getParam().getId(), name, color);
        Help.message("Успешно");

        PageOpenner.INSTANCE.open(MembersListPage.class);
    }

    @Override
    public void addEvents() {
        Button commitButton = MainActivity.INSTANCE.findViewById(R.id.edit_member_commit_button);
        commitButton.setOnClickListener(v -> formCommit());




        final EditText nameField = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
        nameField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                formCommit();
                return true;
            }
            return false;
        });


        nameField.addTextChangedListener(new TextWatcher() {

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
        Button selectColorButton = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
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
        return R.layout.edit_member;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.memberEdit);
    }

    @Override
    protected boolean fillFields() {
        if (!hasParam()) {
            Help.message("Ошибка. Нет участника для редактирования");
            return false;
        }

        MemberBaseTableRow member = t_members.getMemberById(getParam().getId());
        if (member == null) {
            Help.message("Ошибка. Не найден учатсяник с id " + getParam().getId());
            return false;
        }

        EditText edit_name = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
        edit_name.setText(member.name);
        if (member.name.contains(" ")) {
            MainActivity.INSTANCE.findViewById(R.id.memberNameWarning).setVisibility(View.VISIBLE);
        }

        Button buttonColor = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
        buttonColor.setBackgroundColor(member.color);


        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return R.id.edit_member_Name;
    }

    @Override
    protected void helps() {

    }
}
