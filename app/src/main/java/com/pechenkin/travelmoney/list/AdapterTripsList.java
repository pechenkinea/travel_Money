package com.pechenkin.travelmoney.list;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.ViewTripPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.trip.EditTripPage;

public class AdapterTripsList extends BaseAdapter {

    private BaseTableRow[] data;
    private static LayoutInflater inflater = null;
    private boolean showEditButton;

    public AdapterTripsList(Activity a, BaseTableRow[] data, boolean showEditButton) {
        this.data = data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showEditButton = showEditButton;
    }

    public int getCount() {
        return data.length;
    }

    public BaseTableRow getItem(int position) {
        return data[position];
    }

    public long getItemId(int position) {
        return data[position].id;
    }


    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        BaseTableRow row = data[position];

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (showEditButton) {
            holder.editButton.setVisibility(View.VISIBLE);

            holder.editButton.setOnClickListener(v -> {
                PageOpener.INSTANCE.open(EditTripPage.class, new PageParam.BuildingPageParam().setId(row.id).getParam());
            });

        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }


        holder.viewButton.setOnClickListener(v -> {
            PageOpener.INSTANCE.open(ViewTripPage.class, new PageParam.BuildingPageParam().setId(row.id).getParam());
        });


        if (row != null && t_trips.ActiveTrip != null && row.id == t_trips.ActiveTrip.id) {
            holder.check.setImageResource(R.drawable.radio_button_checked_black_18dp);
        } else {
            holder.check.setImageResource(R.drawable.radio_button_unchecked_black_18dp);
        }

        holder.name.setText(row != null ? row.name : "");
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageButton check;
        ImageButton editButton;
        ImageButton viewButton;

        ViewHolder(View convertView) {
            this.name = convertView.findViewById(R.id.lm_name);
            this.check = convertView.findViewById(R.id.lm_check);
            this.editButton = convertView.findViewById(R.id.listEditButton);
            this.viewButton = convertView.findViewById(R.id.listViewButton);
            this.viewButton.setVisibility(View.VISIBLE);
        }
    }
}
