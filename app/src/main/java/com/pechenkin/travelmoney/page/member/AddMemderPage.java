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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.dialog.ColorDialog;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;


/**
 * Created by pechenkin on 19.04.2018.
 * Страница добавления нового участника
 */

public class AddMemderPage extends BaseMemberPage {


    @Override
    void formCommit() {
        EditText etName = MainActivity.INSTANCE.findViewById(R.id.edit_member_Name);

        String name = etName.getText().toString();
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
