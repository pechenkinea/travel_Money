package com.pechenkin.travelmoney.page.member;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.MemberIcons;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
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
        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_members).getParam());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    abstract void formCommit();

    @SuppressLint("DefaultLocale")
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
            MemberBaseTableRow member = t_members.getMemberById(getParam().getId());
            if (member != null) {
                activeIcon = member.icon;
            }

        }

        LinearLayout membersIconsLayout = MainActivity.INSTANCE.findViewById(R.id.membersIconsLayout);
        TextView iconId = MainActivity.INSTANCE.findViewById(R.id.iconId);

        Map<AppCompatImageButton, MemberIcons> buttons = new HashMap<>();

        for (int i = 0; i < MemberIcons.values().length; i++) {
            MemberIcons icon = MemberIcons.values()[i];

            AppCompatImageButton iconButton = new AppCompatImageButton(MainActivity.INSTANCE);
            iconButton.setImageResource(icon.getIcon());

            int dpValueWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    40,
                    MainActivity.INSTANCE.getResources().getDisplayMetrics());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dpValueWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(8, 0, 0, 0);
            iconButton.setLayoutParams(lp);

            iconButton.setScaleType(AppCompatImageButton.ScaleType.CENTER);



            if (activeIcon == icon.getId()){
                iconButton.setBackgroundColor(Color.parseColor("#878787"));
                iconId.setText(String.format("%d", activeIcon));
            }
            else {
                iconButton.setBackgroundResource(R.drawable.background_about_fragment_button);
            }


            buttons.put(iconButton, icon);
            membersIconsLayout.addView(iconButton);

            iconButton.setOnClickListener(view -> {
                for (AppCompatImageButton b : buttons.keySet()) {
                    b.setBackgroundResource(R.drawable.background_about_fragment_button);
                }
                view.setBackgroundColor(Color.parseColor("#878787"));

                if (buttons.containsKey(view)) {
                    iconId.setText(String.format("%d", buttons.get(view).getId()));
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

        if (!t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)) {
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
