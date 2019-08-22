package com.pechenkin.travelmoney.page.main.fragment;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterTripsList;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.trip.AddTripPage;
import com.pechenkin.travelmoney.page.trip.EditTripPage;

import java.util.List;

public class TripsListFragment extends BaseMainPageFragment {

    @Override
    int getViewId() {
        return R.layout.fragment_trips_list;
    }

    @Override
    void setListeners() {

        FloatingActionButton addTripButton = fragmentView.findViewById(R.id.trips_list_add_button);
        addTripButton.setOnClickListener(v -> PageOpener.INSTANCE.open(AddTripPage.class));

        ListView list = fragmentView.findViewById(R.id.catalog_trips_list);
        list.setOnItemClickListener((parent, view, position, id) -> {

            AdapterTripsList adapter = (AdapterTripsList) list.getAdapter();
            Trip item = adapter.getItem(position);
            t_trips.set_active(item.getId());

            MainActivity.INSTANCE.setTitle(item.getName());

            list.setItemChecked(position, true);
            list.invalidateViews();
        });
        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AdapterTripsList tripAdapter = (AdapterTripsList) list.getAdapter();
            Trip trip = tripAdapter.getItem(position);
            PageOpener.INSTANCE.open(EditTripPage.class, new PageParam.BuildingPageParam().setTrip(trip).getParam());
            return true;
        });

    }

    @Override
    public void doAfterRender() {

        List<Trip> allTrips = TripManager.INSTANCE.getAll();
        ListView list = fragmentView.findViewById(R.id.catalog_trips_list);

        if (allTrips.size() == 0) {
            Help.message("Нет данных");
            list.setAdapter(null);
        } else {
            AdapterTripsList tripAdapter = new AdapterTripsList(MainActivity.INSTANCE, allTrips, true);
            list.setAdapter(tripAdapter);
        }
    }

    @Override
    int[] getButtons() {
        return new int[]{
                R.id.trips_list_add_button
        };
    }
}
