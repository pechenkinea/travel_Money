package com.pechenkin.travelmoney.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.summry.Summary;

public class AdapterSumResultList extends BaseAdapter {

    private Summary[] data;
    private static LayoutInflater inflater = null;


    public AdapterSumResultList(Activity a, Summary[] dataList)
    {
        data=dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       
    }
 
    public int getCount() {
        return data.length;
    }
 
    public Summary getItem(int position) {
        try {
            return data[position];
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return data[position].member;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolder holder;
        if(convertView == null)
        {
        	convertView = inflater.inflate(R.layout.list_sum_result_item, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.sum_result_member);
            holder.in = convertView.findViewById(R.id.sum_result_in);
            holder.out = convertView.findViewById(R.id.sum_result_out);
            holder.sum = convertView.findViewById(R.id.sum_result_sum);
          
            convertView.setTag(holder);
        }
        else
        {
        	holder = (ViewHolder) convertView.getTag();
        }

        Summary item = data[position];
        holder.name.setText(t_members.getMemberById(item.member).name);

        holder.in.setText(Help.DoubleToString(item.sumIn));
        holder.out.setText(Help.DoubleToString(item.sumOut));

        double sum = item.sumOut - item.sumIn;
        holder.sum.setText(Help.DoubleToString(sum));
        if (sum < 0)
        {
            holder.sum.setTextColor(Color.parseColor("#b43232"));
        }
        else
        {
            holder.sum.setTextColor(Color.parseColor("#3db432"));
        }

        return convertView;
    }
    
    static class ViewHolder 
    {
        TextView name;
        TextView in;
        TextView out;
        TextView sum;
    }
}
