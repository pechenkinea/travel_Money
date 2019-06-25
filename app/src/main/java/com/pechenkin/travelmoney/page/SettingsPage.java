package com.pechenkin.travelmoney.page;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.t_settings;

/**
 * Created by pechenkin on 11.05.2018.
 * О программе
 */

public class SettingsPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MainPage.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void addEvents() {

        CheckBox hide_show_helps = MainActivity.INSTANCE.findViewById(R.id.checkBox_hide_show_helps);
        hide_show_helps.setOnCheckedChangeListener((buttonView, isChecked) -> t_settings.INSTANCE.setActive(NamespaceSettings.HIDE_ALL_HELP, !isChecked));

        CheckBox group_cost = MainActivity.INSTANCE.findViewById(R.id.checkBox_group_cost);
        group_cost.setOnCheckedChangeListener((buttonView, isChecked) -> t_settings.INSTANCE.setActive(NamespaceSettings.GROUP_COST, isChecked));

        CheckBox group_by_color = MainActivity.INSTANCE.findViewById(R.id.checkBox_group_by_color);
        group_by_color.setOnCheckedChangeListener((buttonView, isChecked) -> t_settings.INSTANCE.setActive(NamespaceSettings.GROUP_BY_COLOR, isChecked));


        final EditText to_member_text_length = MainActivity.INSTANCE.findViewById(R.id.to_member_text_length);
        to_member_text_length.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String length = to_member_text_length.getText().toString();
                t_settings.INSTANCE.set(NamespaceSettings.TO_MEMBER_TEXT_LENGTH, length);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected int getPageId() {
        return R.layout.settings;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.settings);
    }

    @Override
    protected boolean fillFields() {
        CheckBox hide_show_helps = MainActivity.INSTANCE.findViewById(R.id.checkBox_hide_show_helps);
        hide_show_helps.setChecked(!t_settings.INSTANCE.active(NamespaceSettings.HIDE_ALL_HELP));

        CheckBox group_cost = MainActivity.INSTANCE.findViewById(R.id.checkBox_group_cost);
        group_cost.setChecked(t_settings.INSTANCE.active(NamespaceSettings.GROUP_COST));

        CheckBox group_by_color = MainActivity.INSTANCE.findViewById(R.id.checkBox_group_by_color);
        group_by_color.setChecked(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));

        EditText to_member_text_length = MainActivity.INSTANCE.findViewById(R.id.to_member_text_length);
        to_member_text_length.setText(t_settings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }

    @Override
    protected void helps() {

    }
}
