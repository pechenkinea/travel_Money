package com.pechenkin.travelmoney.page.main.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.export.Export;
import com.pechenkin.travelmoney.export.ExportFileTypes;
import com.pechenkin.travelmoney.page.AboutPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.SettingsPage;
import com.pechenkin.travelmoney.page.SumResultListPage;

public class OtherFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_other, container, false);

        Button addMemberButton = fragmentView.findViewById(R.id.settings_button);
        addMemberButton.setOnClickListener(v -> PageOpener.INSTANCE.open(SettingsPage.class));

        Button aboutButton = fragmentView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(v -> PageOpener.INSTANCE.open(AboutPage.class));

        Button statButton = fragmentView.findViewById(R.id.stat_button);
        statButton.setOnClickListener(v -> PageOpener.INSTANCE.open(SumResultListPage.class));

        Button exportButton = fragmentView.findViewById(R.id.export_button);
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
        faqButton.setOnClickListener(v -> Help.alert("Еще не работает"));

        return fragmentView;
    }


}
