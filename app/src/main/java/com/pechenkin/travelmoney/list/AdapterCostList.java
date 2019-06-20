package com.pechenkin.travelmoney.list;

import java.text.SimpleDateFormat;
import java.util.List;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.cost.GroupCost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterCostList extends BaseAdapter {

    private Cost[] data;
    private static LayoutInflater inflater = null;

    public AdapterCostList(Context a, Cost[] data) {
        this.data = data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Cost[] getData() {
        return data;
    }

    public int getCount() {
        return (data != null) ? data.length : 0;
    }

    public Cost getItem(int position) {
        return data[position];
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean isEnabled(int position) {
        Cost item = data[position];
        return item != null && item.id() > 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yy HH:mm");

    @SuppressLint("DefaultLocale")
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Cost song = data[position];


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_summary_group, null);
            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.sum_title); // title
            holder.to_member = convertView.findViewById(R.id.sum_to_member);
            holder.sum_group_sum = convertView.findViewById(R.id.sum_group_sum);
            holder.sum_sum = convertView.findViewById(R.id.sum_sum);
            holder.sum_line = convertView.findViewById(R.id.sum_line);
            holder.sum_comment = convertView.findViewById(R.id.sum_comment);
            holder.have_foto = convertView.findViewById(R.id.sum_havefoto);
            holder.labelHeader = convertView.findViewById(R.id.labelHeader);
            holder.costSeparator = convertView.findViewById(R.id.costSeparator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.costSeparator.setVisibility(View.VISIBLE);

        if (song == null) {
            return convertView;
        }


        convertView.setBackgroundResource(R.drawable.background_normal_item);

        holder.sum_sum.setText("");
        holder.sum_group_sum.setText("");
        holder.sum_line.setText("-->");

        holder.labelHeader.setVisibility(View.INVISIBLE);
        holder.sum_group_sum.setPaintFlags(holder.sum_group_sum.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        String summ = Help.DoubleToString(song.sum());
        holder.sum_group_sum.setText((song.sum() != 0) ? summ : "");
        String comment = "";
        String dateText = "";
        if (song.date() != null) {
            dateText = df2.format(song.date());
        }
        comment += dateText + "  " + song.comment();
        holder.sum_comment.setText(comment);

        holder.sum_line.setText("-->");

        if (song.id() >= 0) {

            if (song.active() == 0) {
                convertView.setBackgroundResource(R.drawable.background_delete_item);
                holder.sum_group_sum.setPaintFlags(holder.sum_group_sum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.sum_group_sum.setText(String.format(" %s ", holder.sum_group_sum.getText()));
            }

            String dir = song.image_dir();
            if (dir.length() > 0)
                holder.have_foto.setVisibility(View.VISIBLE);
            else
                holder.have_foto.setVisibility(View.INVISIBLE);
        } else {
            holder.have_foto.setVisibility(View.INVISIBLE);
        }

        if (song instanceof GroupCost) {
            List<Cost> costs = ((GroupCost) song).getCosts();

            StringBuilder memberText = new StringBuilder();
            StringBuilder to_memberText = new StringBuilder();
            StringBuilder sumText = new StringBuilder();
            StringBuilder sumLineText = new StringBuilder();

            for (Cost cost : costs) {

                MemberBaseTableRow member = t_members.getMemberById(cost.member());
                memberText.append(member.name).append("\n");

                MemberBaseTableRow to_member = t_members.getMemberById(cost.to_member());
                to_memberText.append(to_member.name).append("\n");

                String s = Help.DoubleToString(cost.sum());
                sumText.append((cost.sum() != 0) ? s : "").append("\n");

                sumLineText.append("-->").append("\n");

                holder.title.setTextColor(member.color);
            }

            holder.title.setText(memberText.toString());
            holder.to_member.setText(to_memberText.toString());
            holder.sum_sum.setText(sumText.toString());
            holder.sum_line.setText(sumLineText.toString());

        } else {

            holder.sum_sum.setText((song.sum() != 0) ? summ : "");
            holder.sum_group_sum.setText("");

            MemberBaseTableRow member = t_members.getMemberById(song.member());
            if (member != null) {
                holder.title.setText(member.name);
                holder.title.setTextColor(member.color);
            } else {
                holder.labelHeader.setVisibility(View.VISIBLE);
                holder.labelHeader.setText(song.comment());
                holder.sum_line.setText("");
                holder.title.setText("");
                holder.sum_comment.setText("");
                holder.costSeparator.setVisibility(View.INVISIBLE);
            }

            MemberBaseTableRow to_member = t_members.getMemberById(song.to_member());
            holder.to_member.setText((to_member != null) ? to_member.name : "");
            holder.to_member.setTextColor((to_member != null) ? to_member.color : Color.BLACK);

        }

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView to_member;
        TextView sum_group_sum;
        TextView sum_sum;
        TextView sum_line;
        TextView sum_comment;
        ImageView have_foto;
        TextView labelHeader;
        View costSeparator;

    }


}


