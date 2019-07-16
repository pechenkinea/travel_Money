package com.pechenkin.travelmoney.page.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.cost.add.master.MasterWho;
import com.pechenkin.travelmoney.page.main.CostListBackground;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;


public class CostListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_operation_list, container, false);
        MainActivity.INSTANCE.setTitle(t_trips.ActiveTrip.name);

        FloatingActionButton addCostButton = fragmentView.findViewById(R.id.mainPageAddbutton);
        if (addCostButton != null) {
            addCostButton.setOnClickListener(v -> {
                // Открываем мастер Добавления траты
                PageOpener.INSTANCE.open(MasterWho.class);
            });
        }

        FloatingActionButton mainPageSpeechRecognition = fragmentView.findViewById(R.id.mainPageSpeechRecognition);

        mainPageSpeechRecognition.setOnClickListener(view -> SpeechRecognitionHelper.run(MainActivity.INSTANCE));


        printCostList();
        return fragmentView;
    }


    private void printCostList() {
        CostListBackground costListBackground = new CostListBackground(t_trips.ActiveTrip);
        costListBackground.execute();
    }
}
