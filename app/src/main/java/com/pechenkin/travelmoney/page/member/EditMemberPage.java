package com.pechenkin.travelmoney.page.member;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница редактирования участника
 * Нужно обязательно передать в параметры id сотрудника
 */

public class EditMemberPage extends BaseMemberPage {

    @Override
    void formCommit() {
        TextInputEditText etName = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
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

        String icon = ((TextView)MainActivity.INSTANCE.findViewById(R.id.iconId)).getText().toString();

        int color = Help.getBackgroundColor(buttonColor);

        t_members.edit(getParam().getId(), name, color, (int) Help.StringToDouble(icon));
        Help.message("Успешно");

        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_members).getParam());
    }


    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.memberEdit);
    }



    @Override
    boolean fillFieldsMemberPage() {
        if (!hasParam()) {
            Help.message("Ошибка. Нет участника для редактирования");
            return false;
        }

        MemberBaseTableRow member = t_members.getMemberById(getParam().getId());
        if (member == null) {
            Help.message("Ошибка. Не найден учатсяник с id " + getParam().getId());
            return false;
        }

        TextInputEditText edit_name = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
        edit_name.setText(member.name);
        if (member.name.contains(" ")) {
            MainActivity.INSTANCE.findViewById(R.id.memberNameWarning).setVisibility(View.VISIBLE);
        }

        Button buttonColor = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
        buttonColor.setBackgroundColor(member.color);

        return true;
    }




}
