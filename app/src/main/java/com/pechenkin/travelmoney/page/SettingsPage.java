package com.pechenkin.travelmoney.page;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.TableSettings;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 11.05.2018.
 * О программе
 */

public class SettingsPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_more));
    }



    @Override
    public void addEvents() {

        CheckBox group_by_color = MainActivity.INSTANCE.findViewById(R.id.checkBox_group_by_color);
        group_by_color.setOnCheckedChangeListener((buttonView, isChecked) -> TableSettings.INSTANCE.setActive(NamespaceSettings.GROUP_BY_COLOR, isChecked));


        final EditText to_member_text_length = MainActivity.INSTANCE.findViewById(R.id.to_member_text_length);
        to_member_text_length.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String length = to_member_text_length.getText().toString();
                TableSettings.INSTANCE.set(NamespaceSettings.TO_MEMBER_TEXT_LENGTH, length);
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

        CheckBox group_by_color = MainActivity.INSTANCE.findViewById(R.id.checkBox_group_by_color);
        group_by_color.setChecked(TableSettings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR));

        EditText to_member_text_length = MainActivity.INSTANCE.findViewById(R.id.to_member_text_length);
        to_member_text_length.setText(TableSettings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }


}
