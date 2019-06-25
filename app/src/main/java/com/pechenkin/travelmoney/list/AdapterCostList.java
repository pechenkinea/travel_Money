package com.pechenkin.travelmoney.list;

import java.text.SimpleDateFormat;
import java.util.List;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.cost.GroupCost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterCostList extends BaseAdapter {

    private Cost[] data;
    private static LayoutInflater inflater = null;

    private int to_member_text_length = 12;

    public AdapterCostList(Context a, Cost[] data) {
        this.data = data;
        try {
            to_member_text_length = Integer.parseInt(t_settings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));
            if (to_member_text_length < 4){
                to_member_text_length = 4;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Cost[] getData() {
        return data;
    }

    @Override
    public int getCount() {
        return (data != null) ? data.length : 0;
    }

    @Override
    public Cost getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean isEnabled(int position) {
        Cost item = data[position];
        return item != null && (item.id() > 0 || item instanceof GroupCost);
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




        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_summary_group, null);
            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.sum_title); // title
            holder.to_member = convertView.findViewById(R.id.to_member);
            holder.sum_group_sum = convertView.findViewById(R.id.sum_group_sum);
            holder.sum_sum = convertView.findViewById(R.id.sum_sum);
            holder.sum_line = convertView.findViewById(R.id.sum_line);
            holder.sum_comment = convertView.findViewById(R.id.sum_comment);
            holder.have_foto = convertView.findViewById(R.id.sum_havefoto);
            holder.labelHeader = convertView.findViewById(R.id.labelHeader);
            holder.costSeparator = convertView.findViewById(R.id.costSeparator);
            holder.sumSeparator = convertView.findViewById(R.id.sumSeparator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.costSeparator.setVisibility(View.VISIBLE);
        holder.sumSeparator.setVisibility(View.INVISIBLE);



        Cost song = data[position];

        if (song == null) {
            return convertView;
        }


        convertView.setBackgroundResource(R.drawable.background_normal_item);

        holder.sum_sum.setText("");
        holder.sum_group_sum.setText("");
        holder.sum_line.setText("-->");

        holder.title.setTextColor(Color.BLACK);
        holder.sum_line.setTextColor(Color.BLACK);
        holder.sum_comment.setTextColor(Color.BLACK);

        holder.labelHeader.setVisibility(View.INVISIBLE);
        holder.sum_group_sum.setPaintFlags(holder.sum_group_sum.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.sum_sum.setPaintFlags(holder.sum_group_sum.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        String summ = Help.DoubleToString(song.sum());
        holder.sum_group_sum.setText((song.sum() != 0) ? summ : "");


        String comment = "";
        String dateText = "";
        if (song.date() != null) {
            dateText = df2.format(song.date());
        }
        comment += dateText + "  " + song.comment();
        holder.sum_comment.setText(comment);


        String dir = song.image_dir();
        if (dir.length() > 0)
            holder.have_foto.setVisibility(View.VISIBLE);
        else
            holder.have_foto.setVisibility(View.INVISIBLE);

        if (song instanceof GroupCost) {
            List<Cost> costs = ((GroupCost) song).getCosts();


            String colorDisable = "#CCCCCC";
            int colorDisableColor = Color.parseColor(colorDisable);

            if (costs.size() > 0) {

                MemberBaseTableRow member = t_members.getMemberById(costs.get(0).member());

                if (song.sum() == 0) {
                    holder.title.setTextColor(colorDisableColor);
                    holder.sum_line.setTextColor(colorDisableColor);
                    holder.sum_comment.setTextColor(colorDisableColor);
                }
                else {
                    holder.title.setTextColor(member.color);
                }
                holder.title.setText(member.name);

                StringBuilder to_memberText = new StringBuilder();
                StringBuilder sumText = new StringBuilder();

                for (int i = 0; i < costs.size(); i++) {

                    Cost cost = costs.get(i);
                    MemberBaseTableRow to_member = t_members.getMemberById(cost.to_member());

                    String strColor = String.format("#%06X", 0xFFFFFF & to_member.color);

                    String s = Help.DoubleToString(cost.sum());
                    if (cost.active() != 0) {
                        sumText.append(s);
                    } else {
                        sumText.append("<font color='").append(colorDisable).append("'>").append(s).append("</font>");
                        strColor = colorDisable;
                    }

                    String to_memberName = to_member.name;
                    if (to_memberName.length() > to_member_text_length) {
                        to_memberName = to_memberName.substring(0, to_member_text_length - 3).trim() + "...";
                    }

                    String to_memberLine = "<font color='" + strColor + "'>" + to_memberName + "</font>";

                    to_memberText.append(to_memberLine);

                    if (i < costs.size() - 1) {
                        to_memberText.append("<br>");
                        sumText.append("<br>");
                    }
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.to_member.setText(Html.fromHtml(to_memberText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
                    holder.sum_sum.setText(Html.fromHtml(sumText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
                } else {
                    holder.to_member.setText(Html.fromHtml(to_memberText.toString()), TextView.BufferType.SPANNABLE);
                    holder.sum_sum.setText(Html.fromHtml(sumText.toString()), TextView.BufferType.SPANNABLE);
                }

            }

            if (costs.size() == 1) {
                holder.sum_group_sum.setText("");
            } else {
                holder.sumSeparator.setVisibility(View.VISIBLE);
            }

        } else {


            holder.sum_sum.setText((song.sum() != 0) ? summ : "");
            holder.sum_group_sum.setText("");

            if (song.active() == 0) {
                convertView.setBackgroundResource(R.drawable.background_delete_item);
                holder.sum_sum.setPaintFlags(holder.sum_group_sum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.sum_sum.setText(String.format(" %s ", holder.sum_sum.getText()));  //пробелы нужны для красоты, что бы зачеркивание выходило за рамки текста
            }

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
        View sumSeparator;

    }


}


