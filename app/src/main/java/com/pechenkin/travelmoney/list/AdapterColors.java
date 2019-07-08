package com.pechenkin.travelmoney.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;

public class AdapterColors extends BaseAdapter {

    private BaseTableRow[] data;
    private static LayoutInflater inflater = null;
    private String text;


    public AdapterColors(Context a, BaseTableRow[] data, String text) {
        this.data = data;
        this.text = text;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (data != null) ? data.length : 0;
    }

    @Override
    public BaseTableRow getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint({"DefaultLocale", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_color, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BaseTableRow colorItem = data[position];

        holder.colorTextView.setText(text);
        holder.colorTextView.setTextColor((int)colorItem.id);
        return convertView;
    }

    static class ViewHolder {
        TextView colorTextView;

        ViewHolder(View convertView) {
            this.colorTextView = convertView.findViewById(R.id.colorTextView);
        }
    }


}


