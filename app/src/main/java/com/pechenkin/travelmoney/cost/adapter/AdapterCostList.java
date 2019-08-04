package com.pechenkin.travelmoney.cost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pechenkin.travelmoney.R;


public class AdapterCostList extends BaseAdapter {

    private final CostListItem[] data;
    private static LayoutInflater inflater = null;



    public AdapterCostList(Context a, CostListItem[] data) {
        this.data = data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (data != null) ? data.length : 0;
    }

    @Override
    public CostListItem getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean isEnabled(int position) {
        CostListItem item = data[position];
        return item != null && item.isClicked();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CostListItem cost = data[position];

        CostListViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_summary_group, parent, false);
            holder = new CostListViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CostListViewHolder) convertView.getTag();
        }

        holder.toDefaultView();


        if (cost == null) {
            return convertView;
        }

        cost.render(holder);


        return convertView;
    }



}


