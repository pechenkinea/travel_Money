package com.pechenkin.travelmoney.page.main.fragment;

import android.app.AlertDialog;
import android.widget.Button;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.export.Export;
import com.pechenkin.travelmoney.export.ExportFileTypes;
import com.pechenkin.travelmoney.metering.CostCreator;
import com.pechenkin.travelmoney.page.AboutPage;
import com.pechenkin.travelmoney.page.FaqPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.SettingsPage;
import com.pechenkin.travelmoney.page.SumResultListPage;

public class OtherFragment extends BaseMainPageFragment {

    @Override
    int getViewId() {
        return R.layout.fragment_other;
    }

    @Override
    void setListeners() {



        Button addMemberButton = fragmentView.findViewById(R.id.settings_button);
        addMemberButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_24, 0, 0);
        addMemberButton.setOnClickListener(v -> PageOpener.INSTANCE.open(SettingsPage.class));

        Button aboutButton = fragmentView.findViewById(R.id.about_button);
        aboutButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_info_24, 0, 0);
        aboutButton.setOnClickListener(v -> PageOpener.INSTANCE.open(AboutPage.class));

        Button statButton = fragmentView.findViewById(R.id.stat_button);
        statButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_stat_24, 0, 0);
        statButton.setOnClickListener(v -> PageOpener.INSTANCE.open(SumResultListPage.class));

        Button exportButton = fragmentView.findViewById(R.id.export_button);
        exportButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_share_24, 0, 0);
        exportButton.setOnClickListener(v -> {

            String[] fileTypes = new String[ExportFileTypes.values().length];
            for (int i = 0; i < ExportFileTypes.values().length; i++) {
                fileTypes[i] = ExportFileTypes.values()[i].toString();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
            builder.setTitle("Выберите тип файла:");

            builder.setItems(fileTypes, (dialog, which) -> {
                Export.export(t_trips.ActiveTrip, ExportFileTypes.values()[which]);
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        });

        Button faqButton = fragmentView.findViewById(R.id.faq_button);
        faqButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_help_24, 0, 0);
        faqButton.setOnClickListener(v -> PageOpener.INSTANCE.open(FaqPage.class));

        /*
        Button add_many = fragmentView.findViewById(R.id.add_many);
        add_many.setOnClickListener(v -> CostCreator.createBigCostList());
        */
    }

    @Override
    public void doAfterRender() {

    }

    @Override
    int[] getButtons() {
        return new int[0];
    }


}
