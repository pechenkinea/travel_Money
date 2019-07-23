package com.pechenkin.travelmoney.list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.ShortCost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterCostList extends RecyclerView.Adapter {


    private List<ShortCost> data;

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public ShortCost getItem(ShortCost cost) {
        for (ShortCost c : data) {
            if (c.equals(cost))
                return c;
        }
        return null;
    }

    public ShortCost getItem(int position) {
        try {
            return data.get(position);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<ShortCost> getData() {
        return data;
    }

    private static LayoutInflater inflater = null;

    public RecyclerAdapterCostList(Context a, List<ShortCost> data) {
        this.data = data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item_summary, null);


        ViewHolder holder = new ViewHolder(v);

        holder.editButton.setOnClickListener(v1 -> {
            final RecyclerView listView;

            View listItem = (View) v1.getParent().getParent().getParent();

            try {
                listView = (RecyclerView) listItem.getParent();
            } catch (Exception ex) {
                Help.alertError(ex.getMessage());
                return;
            }

            final RecyclerAdapterCostList adapter = (RecyclerAdapterCostList) listView.getAdapter();
            if (adapter == null) {
                Help.alertError("adapter is null");
                return;
            }
            final ShortCost item = adapter.getItem(listView.getChildLayoutPosition(listItem));

            if (item != null && item.member() > -1) {

                final EditText input = new EditText(MainActivity.INSTANCE);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                input.setText(((TextView) listItem.findViewById(R.id.sum_sum)).getText());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                builder.setTitle("")
                        .setCancelable(false)
                        .setPositiveButton("Ок", (dialog, which) -> commitEditSum(input, item, adapter, dialog, listView, false))
                        .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel())
                        .setNeutralButton("По умолчанию", (dialog, which) -> commitEditSum(input, item, adapter, dialog, listView, true));


                final AlertDialog alert = builder.create();
                alert.setView(input);
                if (alert.getWindow() != null)
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                input.setOnEditorActionListener((v11, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        commitEditSum(input, item, adapter, alert, listView, false);
                        return true;
                    }
                    return false;
                });

                alert.show();

                Help.setActiveEditText(input, true);

            }

        });


        return new ViewHolder(v);
    }


    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        ShortCost song = data.get(h.getAdapterPosition());

        ViewHolder holder = (ViewHolder) h;


        holder.labelHeader.setVisibility(View.INVISIBLE);
        holder.sum_sum.setPaintFlags(holder.sum_sum.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));


        String summ = Help.DoubleToString(song.sum());

        if (song.isChange()) {
            holder.sum_sum.setText(Html.fromHtml("<b>" + summ + "</b> "));
        } else {
            holder.sum_sum.setText((song.sum() != 0) ? summ : "");
        }


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
                //Делаем зачеркнутым и добавляем пробелы
                holder.sum_sum.setPaintFlags(holder.sum_sum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.sum_sum.setText(String.format(" %s ", holder.sum_sum.getText()));
            }

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
            holder.editButton.setVisibility(View.INVISIBLE);
        }

        MemberBaseTableRow to_member = t_members.getMemberById(song.to_member());
        holder.to_member.setText((to_member != null) ? to_member.name : "");
        holder.to_member.setTextColor((to_member != null) ? to_member.color : Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView to_member;
        TextView sum_sum;
        TextView sum_line;
        TextView sum_comment;
        TextView labelHeader;
        View costSeparator;
        ImageButton editButton;


        ViewHolder(View convertView) {
            super(convertView);
            this.title = convertView.findViewById(R.id.sum_title); // title
            this.to_member = convertView.findViewById(R.id.to_member);
            this.sum_sum = convertView.findViewById(R.id.sum_sum);
            this.sum_line = convertView.findViewById(R.id.sum_line);
            this.sum_comment = convertView.findViewById(R.id.sum_comment);
            this.labelHeader = convertView.findViewById(R.id.labelHeader);
            this.costSeparator = convertView.findViewById(R.id.costSeparator);
            this.editButton = convertView.findViewById(R.id.listEditButton);
        }
    }

    /**
     * Обработка редактирования суммы одной строки
     */
    private static void commitEditSum(final EditText input, final ShortCost item, final RecyclerAdapterCostList adapter, DialogInterface dialog, final RecyclerView listView, boolean toDefault) {
        double editSum = Help.StringToDouble(String.valueOf(input.getText()));

        double sumGroup = 0;

        List<ShortCost> costGroup = new ArrayList<>();

        if (toDefault) {
            item.setChange(false);
            costGroup.add(item);
        } else {
            sumGroup = item.sum();
            item.sum = editSum;
            item.setChange(true);
            sumGroup = sumGroup - editSum;
        }

        int groupId = item.getGroupId();

        List<ShortCost> costs = adapter.getData();


        for (ShortCost c : costs) {
            if (c.isChange())
                continue;

            if (c.getGroupId() == groupId) {
                if (!c.equals(item))
                    costGroup.add(c);
                sumGroup += c.sum();
            }
        }

        if (sumGroup < 0)
            sumGroup = 0;


        for (ShortCost c : costGroup) {
            c.sum = sumGroup / costGroup.size();
        }

        dialog.cancel();
        listView.setAdapter(null);
        listView.setAdapter(adapter);


        View refresh_button = MainActivity.INSTANCE.findViewById(R.id.add_costs_list_refresh_button);
        if (refresh_button != null) {
            refresh_button.setVisibility(View.VISIBLE);
        }
    }


}


