package com.pechenkin.travelmoney.page.member;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.utils.RunWithProgressBar;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница редактирования участника
 * Нужно обязательно передать в параметры id сотрудника
 */

public class EditMemberPage extends BaseMemberPage {

    @Override
    void formCommit() {
        TextInputEditText etName = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
        String name = getTextInputEditText(etName);

        if (name.length() < 1) {
            Help.message("Введите имя");
            Help.setActiveEditText(getFocusFieldId());
            return;
        }


        Member findMember = TripManager.INSTANCE.getActiveTrip().getMemberByName(name);
        if (findMember != null && !findMember.equals(getParam().getMember())) {
            Help.message("Имя занято другим участником");
            Help.setActiveEditText(getFocusFieldId());
            return;
        }


        Button buttonColor = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);

        String icon = ((TextView) MainActivity.INSTANCE.findViewById(R.id.iconId)).getText().toString();

        int color = Help.getBackgroundColor(buttonColor);

        Member member = getParam().getMember();
        if (member != null) {

            new RunWithProgressBar<>(
                    () -> {
                        member.edit(name, color, (int) Help.StringToDouble(icon));
                        return null;
                    },
                    o -> PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_members))
            );


        } else {
            Help.message("Ошибка");
        }


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

        Member member = getParam().getMember();
        if (member == null) {
            Help.message("Ошибка. Не задан участник.");
            return false;
        }

        TextInputEditText edit_name = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);
        edit_name.setText(member.getName());
        if (member.getName().contains(" ")) {
            MainActivity.INSTANCE.findViewById(R.id.memberNameWarning).setVisibility(View.VISIBLE);
        }

        Button buttonColor = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
        buttonColor.setBackgroundColor(member.getColor());

        return true;
    }


}
