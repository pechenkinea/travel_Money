package com.pechenkin.travelmoney.page.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.result.TripsQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterTripsList;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.trip.EditTripPage;

public class TripsListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_trips_list, container, false);

        TripsQueryResult allTrips = t_trips.getAll();
        ListView list = fragmentView.findViewById(R.id.catalog_trips_list);

        if (!allTrips.hasRows()) {
            Help.message("Нет данных");
            list.setAdapter(null);
        } else {
            AdapterTripsList tripAdapter = new AdapterTripsList(MainActivity.INSTANCE, allTrips.getAllRows(), true);
            list.setAdapter(tripAdapter);
        }

        list.setOnItemClickListener((parent, view, position, id) -> {

                    AdapterTripsList adapter = (AdapterTripsList) list.getAdapter();
                    BaseTableRow item = adapter.getItem(position);
                    t_trips.set_active(item.id);

                    list.setItemChecked(position, true);
                    list.invalidateViews();
                }

        );

        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AdapterTripsList tripAdapter = (AdapterTripsList) list.getAdapter();
            BaseTableRow trip = tripAdapter.getItem(position);
            PageOpener.INSTANCE.open(EditTripPage.class, new PageParam.BuildingPageParam().setId(trip.id).getParam());
            return true;
        });


        return fragmentView;

    }

}
