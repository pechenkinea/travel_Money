package com.pechenkin.travelmoney.page.member;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;


/**
 * Created by pechenkin on 19.04.2018.
 * Страница добавления нового участника
 */

public class AddMemderPage extends BaseMemberPage {


    @Override
    void formCommit() {
        TextInputEditText etName = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);

        String name = getTextInputEditText(etName);
        if (t_members.isAdded(name)) {
            Help.message("Участник с таким именем уже добавлен");
            Help.setActiveEditText(getFocusFieldId());
            return;
        }

        if (name.length() > 0) {
            Button buttonColor = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
            Drawable background = buttonColor.getBackground();
            int color = 0;
            if (background instanceof ColorDrawable)
                color = ((ColorDrawable) background).getColor();


            String icon = ((TextView)MainActivity.INSTANCE.findViewById(R.id.iconId)).getText().toString();

            long m_id = t_members.add(name, color, (int) Help.StringToDouble(icon));
            Help.message("Успешно");
            t_trips.addMemberInTrip(t_trips.ActiveTrip.id, m_id);

            clickBackButton();
        } else {
            Help.message("Введите имя");
            Help.setActiveEditText(getFocusFieldId());
        }
    }

    @Override
    boolean fillFieldsMemberPage() {
        return true;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.addNewMember);
    }


}
