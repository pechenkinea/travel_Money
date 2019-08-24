package com.pechenkin.travelmoney.page.member;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.utils.MemberIcons;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.SettingsTable;
import com.pechenkin.travelmoney.dialog.ColorDialog;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.HashMap;
import java.util.Map;

abstract class BaseMemberPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_members));
    }




    abstract void formCommit();

    @Override
    public void addEvents() {
        FloatingActionButton commitButton = MainActivity.INSTANCE.findViewById(R.id.edit_member_commit_button);
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
                    MainActivity.INSTANCE.findViewById(R.id.memberNameWarning).setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Выбор цвета
        Button selectColorButton = MainActivity.INSTANCE.findViewById(R.id.buttonSelectColor);
        selectColorButton.setOnClickListener(v -> {
            String text = nameField.getText().toString();
            if (text.length() == 0) {
                text = "Имя не задано";
            }

            ColorDialog.selectColor(MainActivity.INSTANCE, text, selectColorButton::setBackgroundColor);
        });


        int activeIcon = 0;
        if (hasParam()) {
            Member member = getParam().getMember();
            if (member != null) {
                activeIcon = member.getIcon();
            }

        }

        LinearLayout membersIconsLayout = MainActivity.INSTANCE.findViewById(R.id.membersIconsLayout);
        TextView iconId = MainActivity.INSTANCE.findViewById(R.id.iconId);

        Map<View, MemberIcons> buttons = new HashMap<>();

        for (int i = 0; i < MemberIcons.values().length; i++) {
            MemberIcons icon = MemberIcons.values()[i];

            AppCompatImageButton iconButton = new AppCompatImageButton(MainActivity.INSTANCE);
            iconButton.setImageResource(icon.getIcon());

            int dpValueWidth = Help.dpToPx(40);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpValueWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 8, 0);
            iconButton.setLayoutParams(lp);

            iconButton.setScaleType(AppCompatImageButton.ScaleType.CENTER);


            if (activeIcon == icon.getId()) {
                iconButton.setBackgroundColor(Color.parseColor("#878787"));
                String iconText = "" + activeIcon;
                iconId.setText(iconText);
            } else {
                iconButton.setBackgroundResource(R.drawable.background_about_fragment_button);
            }


            buttons.put(iconButton, icon);
            membersIconsLayout.addView(iconButton);

            iconButton.setOnClickListener(view -> {
                for (View b : buttons.keySet()) {
                    b.setBackgroundResource(R.drawable.background_about_fragment_button);
                }
                view.setBackgroundColor(Color.parseColor("#878787"));

                MemberIcons viewButton = buttons.get(view);
                if (viewButton != null) {
                    String iconText = "" + viewButton.getId();
                    iconId.setText(iconText);
                }
            });
        }

    }

    @Override
    protected int getPageId() {
        return R.layout.edit_member;
    }


    @Override
    protected boolean fillFields() {

        if (!SettingsTable.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)) {
            MainActivity.INSTANCE.findViewById(R.id.colorHelpMessage).setVisibility(View.VISIBLE);
        }

        return fillFieldsMemberPage();
    }

    abstract boolean fillFieldsMemberPage();

    @Override
    protected int getFocusFieldId() {
        return R.id.edit_member_Name;
    }


}
