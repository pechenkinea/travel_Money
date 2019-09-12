package com.pechenkin.travelmoney.list;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.TripStore;
import com.pechenkin.travelmoney.bd.firestore.TripSync;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.ViewTripPage;
import com.pechenkin.travelmoney.page.trip.EditTripPage;
import com.pechenkin.travelmoney.utils.RunWithProgressBar;

import java.util.List;

public class AdapterTripsList extends BaseAdapter {

    private final List<Trip> data;
    private static LayoutInflater inflater = null;
    private final boolean showEditButton;

    public AdapterTripsList(Activity a, List<Trip> data, boolean showEditButton) {
        this.data = data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showEditButton = showEditButton;
    }

    public int getCount() {
        return data.size();
    }

    public Trip getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Trip row = data.get(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (showEditButton) {
            holder.editButton.setVisibility(View.VISIBLE);

            holder.editButton.setOnClickListener(v -> PageOpener.INSTANCE.open(EditTripPage.class, new PageParam().setTrip(row)));

        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }


        holder.viewButton.setOnClickListener(v -> PageOpener.INSTANCE.open(ViewTripPage.class, new PageParam().setTrip(row)));


        if (TripManager.INSTANCE.isActive(row)) {
            holder.check.setImageResource(R.drawable.radio_button_checked_black_18dp);
        } else {
            holder.check.setImageResource(R.drawable.radio_button_unchecked_black_18dp);
        }

        if (row.getTripStore() == TripStore.FIRESTORE) {
            holder.updateProject.setVisibility(View.VISIBLE);

            holder.updateProject.setOnClickListener(view -> new RunWithProgressBar<>(
                    () -> {
                        TripSync.sync(row.getUUID());
                        if (TripManager.INSTANCE.isActive(row)) {
                            TripManager.INSTANCE.updateActiveTrip();
                        }
                        return null;
                    },
                    null));
        } else {
            holder.updateProject.setVisibility(View.GONE);
        }

        holder.name.setText(row.getName());
        return convertView;
    }

    static class ViewHolder {
        final TextView name;
        final ImageButton check;
        final ImageButton editButton;
        final ImageButton viewButton;
        final ImageButton updateProject;

        ViewHolder(View convertView) {
            this.name = convertView.findViewById(R.id.lm_name);
            this.check = convertView.findViewById(R.id.lm_check);
            this.editButton = convertView.findViewById(R.id.listEditButton);
            this.viewButton = convertView.findViewById(R.id.listViewButton);
            this.updateProject = convertView.findViewById(R.id.updateProject);
            this.viewButton.setVisibility(View.VISIBLE);
        }
    }
}
