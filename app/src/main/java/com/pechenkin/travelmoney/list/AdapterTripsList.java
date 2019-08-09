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
import com.pechenkin.travelmoney.bd.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.ViewTripPage;
import com.pechenkin.travelmoney.page.trip.EditTripPage;

public class AdapterTripsList extends BaseAdapter {

    private final IdAndNameTableRow[] data;
    private static LayoutInflater inflater = null;
    private final boolean showEditButton;

    public AdapterTripsList(Activity a, IdAndNameTableRow[] data, boolean showEditButton) {
        this.data = data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showEditButton = showEditButton;
    }

    public int getCount() {
        return data.length;
    }

    public IdAndNameTableRow getItem(int position) {
        return data[position];
    }

    public long getItemId(int position) {
        return data[position].id;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        IdAndNameTableRow row = data[position];

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (showEditButton) {
            holder.editButton.setVisibility(View.VISIBLE);

            holder.editButton.setOnClickListener(v -> PageOpener.INSTANCE.open(EditTripPage.class, new PageParam.BuildingPageParam().setId(row.id).getParam()));

        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }


        holder.viewButton.setOnClickListener(v -> PageOpener.INSTANCE.open(ViewTripPage.class, new PageParam.BuildingPageParam().setId(row.id).getParam()));


        if (row != null && t_trips.getActiveTrip() != null && row.id == t_trips.getActiveTrip().id) {
            holder.check.setImageResource(R.drawable.radio_button_checked_black_18dp);
        } else {
            holder.check.setImageResource(R.drawable.radio_button_unchecked_black_18dp);
        }

        holder.name.setText(row != null ? row.name : "");
        return convertView;
    }

    static class ViewHolder {
        final TextView name;
        final ImageButton check;
        final ImageButton editButton;
        final ImageButton viewButton;

        ViewHolder(View convertView) {
            this.name = convertView.findViewById(R.id.lm_name);
            this.check = convertView.findViewById(R.id.lm_check);
            this.editButton = convertView.findViewById(R.id.listEditButton);
            this.viewButton = convertView.findViewById(R.id.listViewButton);
            this.viewButton.setVisibility(View.VISIBLE);
        }
    }
}
