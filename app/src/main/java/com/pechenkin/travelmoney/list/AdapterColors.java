package com.pechenkin.travelmoney.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.IdAndNameTableRow;

public class AdapterColors extends BaseAdapter {

    private final IdAndNameTableRow[] data;
    private static LayoutInflater inflater = null;
    private final String text;


    public AdapterColors(Context a, IdAndNameTableRow[] data, String text) {
        this.data = data;
        this.text = text;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return (data != null) ? data.length : 0;
    }

    @Override
    public IdAndNameTableRow getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_color, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        IdAndNameTableRow colorItem = data[position];

        holder.colorTextView.setText(text);
        holder.colorTextView.setTextColor((int)colorItem.id);
        return convertView;
    }

    static class ViewHolder {
        final TextView colorTextView;

        ViewHolder(View convertView) {
            this.colorTextView = convertView.findViewById(R.id.colorTextView);
        }
    }


}


