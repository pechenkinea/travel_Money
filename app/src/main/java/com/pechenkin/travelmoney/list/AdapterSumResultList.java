package com.pechenkin.travelmoney.list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

public class AdapterSumResultList extends BaseAdapter {

    private final Total.MemberSum[] data;
    private static LayoutInflater inflater = null;


    public AdapterSumResultList(Activity a, Total.MemberSum[] dataList) {
        data = dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return data.length;
    }

    public Total.MemberSum getItem(int position) {
        try {
            return data[position];
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return data[position].getMemberId();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_sum_result_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Total.MemberSum item = data[position];
        holder.name.setText(t_members.getMemberById(item.getMemberId()).name);

        holder.in.setText(Help.doubleToString(item.getSumIn()));
        holder.out.setText(Help.doubleToString(item.getSumOut()));

        double sum = item.getSumOut() - item.getSumIn();
        holder.sum.setText(Help.doubleToString(sum));
        if (sum < 0) {
            holder.sum.setTextColor(MainActivity.INSTANCE.getResources().getColor(R.color.red));
        } else {
            holder.sum.setTextColor(MainActivity.INSTANCE.getResources().getColor(R.color.green));
        }

        return convertView;
    }

    static class ViewHolder {
        final TextView name;
        final TextView in;
        final TextView out;
        final TextView sum;
        ViewHolder(View convertView){
            this.name = convertView.findViewById(R.id.sum_result_member);
            this.in = convertView.findViewById(R.id.sum_result_in);
            this.out = convertView.findViewById(R.id.sum_result_out);
            this.sum = convertView.findViewById(R.id.sum_result_sum);

        }
    }
}
