package com.pechenkin.travelmoney.page.trip;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.bd.table.result.TripsQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.list.AdapterTripsList;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.MainPage;
import com.pechenkin.travelmoney.page.PageOpenner;
import com.pechenkin.travelmoney.page.PageParam;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница со списком поездок
 */

public class TripsListPage extends ListPage {
    @Override
    protected int getPageId() {
        return R.layout.view_list_trips;
}

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.tripsList);
    }


    @Override
    public void addEvents() {
        super.addEvents();

        Button addTripButton = MainActivity.INSTANCE.findViewById(R.id.trips_list_add_button);
        addTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageOpenner.INSTANCE.open(AddTripPage.class);
            }
        });

        Button member_list_commit = MainActivity.INSTANCE.findViewById(R.id.trip_list_commit);
        member_list_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageOpenner.INSTANCE.open(MainPage.class);
            }
        });
    }

    @Override
    protected boolean fillFields() {

        TripsQueryResult allTrips = t_trips.getAll();
        ListView list1 =  MainActivity.INSTANCE.findViewById(R.id.catalog_trips_list);

        if (!allTrips.hasRows())
        {
            Help.message("Нет данных");
            list1.setAdapter(null);
        }
        else
        {

            AdapterTripsList tripAdapter = new AdapterTripsList(MainActivity.INSTANCE, allTrips.getAllRows(), true);
            list1.setAdapter(tripAdapter);
        }


        return true;
    }

    @Override
    protected void helps() {
        if (t_settings.INSTANCE.active(NamespaceSettings.TRIPS_LIST_HELP))
        {
            TextView helpView = MainActivity.INSTANCE.findViewById(R.id.helpHintCheckTrip);
            helpView.setVisibility(View.VISIBLE);

            TextView helpAddButton = MainActivity.INSTANCE.findViewById(R.id.helpHintAddTrip);
            helpAddButton.setVisibility(View.VISIBLE);

            TextView helpHintCommitMemberList = MainActivity.INSTANCE.findViewById(R.id.helpHintCommitTripList);
            helpHintCommitMemberList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getListViewId() {
        return R.id.catalog_trips_list;
    }

    @Override
    protected void onItemClick(ListView list, AdapterView<?> a, View view, int position, long id) {

        AdapterTripsList adapter =  (AdapterTripsList)list.getAdapter();
        BaseTableRow item = adapter.getItem(position);
        t_trips.set_active(item.id);

        list.setItemChecked(position, true);
        list.invalidateViews();

        //fillFields();
    }

    @Override
    protected boolean onItemLongClick(ListView list, AdapterView<?> adapter, View view, int position, long arg3) {
        AdapterTripsList tripAdapter = (AdapterTripsList)list.getAdapter();
        BaseTableRow trip = tripAdapter.getItem(position);
        PageOpenner.INSTANCE.open(EditTripPage.class, new PageParam.BuildingPageParam().setId(trip.id).getParam());
        return true;
    }
}
