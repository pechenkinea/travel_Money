package com.pechenkin.travelmoney.page.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.main.CostListBackground;


public class CostListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operation_list, container, false);
        MainActivity.INSTANCE.setTitle(t_trips.ActiveTrip.name);
        printCostList();
        return view;
    }


    private void printCostList() {
        CostListBackground costListBackground = new CostListBackground(t_trips.ActiveTrip);
        costListBackground.execute();
    }
}
