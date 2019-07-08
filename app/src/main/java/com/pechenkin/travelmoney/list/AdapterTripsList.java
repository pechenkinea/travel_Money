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
import com.pechenkin.travelmoney.page.MainPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.trip.EditTripPage;

public class AdapterTripsList extends BaseAdapter {

    private BaseTableRow[] data;
    private static LayoutInflater inflater = null;
    private  boolean showEditButton;
 
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
        if(convertView == null)
        {
        	convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.lm_name);
            holder.check = convertView.findViewById(R.id.lm_check);
            holder.editButton =  convertView.findViewById(R.id.listEditButton);
            if (showEditButton) {
                holder.editButton.setOnClickListener(v -> {

                    BaseTableRow item;
                    try {
                        View parentRow = (View) v.getParent();
                        ListView listView = (ListView) parentRow.getParent();
                        int position1 = listView.getPositionForView(parentRow);
                        item = getItem(position1);
                    } catch (Exception ex) {
                        Help.alert(ex.getMessage());
                        return;
                    }

                    if (item != null) {
                        PageOpener.INSTANCE.open(EditTripPage.class, new PageParam.BuildingPageParam().setId(item.id).getParam());
                    }
                });
            }

            holder.viewButton = convertView.findViewById(R.id.listViewButton);
            holder.viewButton.setVisibility(View.VISIBLE);
            holder.viewButton.setOnClickListener(v -> {

                BaseTableRow item;
                try {
                    View parentRow = (View) v.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    int position12 = listView.getPositionForView(parentRow);
                    item = getItem(position12);
                } catch (Exception ex) {
                    Help.alert(ex.getMessage());
                    return;
                }

                if (item != null) {
                    PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(item.id).getParam());
                }
            });

            convertView.setTag( holder );
        }
        else
        {
        	holder = (ViewHolder) convertView.getTag();
        }


        if (showEditButton)
        {
            holder.editButton.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.editButton.setVisibility(View.INVISIBLE);
        }

        BaseTableRow row = data[position];

        if (row != null && t_trips.ActiveTrip != null && row.id == t_trips.ActiveTrip.id)
        {
            holder.check.setVisibility(View.VISIBLE);
		}
        else 
        {
            holder.check.setVisibility(View.INVISIBLE);
		}
        
        holder.name.setText(row!=null?row.name:"");
        return convertView;
    }
    
    static class ViewHolder 
    {
    	TextView name;
        ImageButton check;
        ImageButton editButton;
        ImageButton viewButton;
    }
}
