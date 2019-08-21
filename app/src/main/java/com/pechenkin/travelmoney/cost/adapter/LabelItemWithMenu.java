package com.pechenkin.travelmoney.cost.adapter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.t_settings;

/**
 * Created by pechenkin on 06.04.2018.
 * Для итогов и дополнительных строк листа операций
 */

public class LabelItemWithMenu extends LabelItem {


    public LabelItemWithMenu(String label) {
        super(label);
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {
        super.render(holder);

        holder.getMiniMenu().setVisibility(View.VISIBLE);

        holder.getMiniMenu().setColorFilter(Color.WHITE);

        holder.getMiniMenu().setOnClickListener(view -> {

            boolean currentCheckValue = t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR);

            final CheckBox input = new CheckBox(MainActivity.INSTANCE);

            input.setText(MainActivity.INSTANCE.getString(R.string.group_by_color_on_off));
            input.setChecked(currentCheckValue);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);


            input.setLayoutParams(lp);

            int padding = Help.dpToPx(16);
            input.setPadding(padding, padding, padding, padding);


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
            builder.setTitle("").setCancelable(false);


            final AlertDialog alert = builder.create();
            alert.setView(input);
            if (alert.getWindow() != null) {
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }


            input.setOnCheckedChangeListener((compoundButton, checked) -> t_settings.INSTANCE.setActive(NamespaceSettings.GROUP_BY_COLOR, checked));

            alert.setCanceledOnTouchOutside(true);
            alert.setOnCancelListener(dialogInterface -> {

                if (currentCheckValue != t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR)){
                    MainActivity.INSTANCE.refresh();
                }

            });
            alert.show();


        });
    }
}
