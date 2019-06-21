package com.pechenkin.travelmoney.list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.cost.ShortCost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterCostList extends RecyclerView.Adapter {


    private List<ShortCost> data;
    public void remove(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public ShortCost getItem(ShortCost cost)
    {
        for (ShortCost c: data) {
            if (c.equals(cost))
                return c;
        }
        return null;
    }

    public ShortCost getItem(int position)
    {
        try {
            return data.get(position);
        }
        catch (Exception ex)
        {
            return  null;
        }
    }

    public  List<ShortCost> getData()
    {
        return  data;
    }

    private static LayoutInflater inflater = null;

    public RecyclerAdapterCostList(Context a, List<ShortCost> data) {
        this.data=data;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item_summary, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final RecyclerView listView;
                try {
                    listView = (RecyclerView) v.getParent();
                }
                catch (Exception ex)
                {
                    Help.alertError(ex.getMessage());
                    return;
                }

                final RecyclerAdapterCostList adapter = (RecyclerAdapterCostList)listView.getAdapter();
                final ShortCost item = adapter.getItem(listView.getChildLayoutPosition(v));

                if (item != null && item.member() > -1) {

                    final EditText input = new EditText(MainActivity.INSTANCE);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    input.setText(((TextView)v.findViewById(R.id.sum_sum)).getText());

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                    builder.setTitle("")
                            .setCancelable(false)
                            .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    commitEditSum(input,item,adapter,dialog,listView, false);
                                }
                            })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setNeutralButton("По умолчанию", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    commitEditSum(input, item, adapter, dialog, listView, true);
                                }
                            });


                    final AlertDialog alert = builder.create();
                    alert.setView(input);
                    if (alert.getWindow() != null)
                        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                    input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                commitEditSum(input, item, adapter, alert, listView, false);
                                return true;
                            }
                            return false;
                        }
                    });

                    alert.show();

                    Help.setActiveEditText(input, true);

                }


            }
        });
        return new ViewHolder(v);
    }



    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        ShortCost song = data.get(h.getAdapterPosition());

        ViewHolder holder = (ViewHolder)h;


        holder.labelHeader.setVisibility(View.INVISIBLE);
        holder.sum_sum.setPaintFlags( holder.sum_sum.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));




        String summ = Help.DoubleToString(song.sum());

        if (song.isChange())
        {
            holder.sum_sum.setText(Html.fromHtml("<b>" + summ + "</b> "));
        }
        else
        {
            holder.sum_sum.setText((song.sum() != 0)?summ:"");
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

            String dir = song.image_dir();
            if (dir.length() > 0)
                holder.have_foto.setVisibility(View.VISIBLE);
            else
                holder.have_foto.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.have_foto.setVisibility(View.INVISIBLE);
        }



        MemberBaseTableRow member = t_members.getMemberById(song.member());
        if (member!=null) {
            holder.title.setText( member.name);
            holder.title.setTextColor(member.color);
        }
        else
        {
            holder.labelHeader.setVisibility(View.VISIBLE);
            holder.labelHeader.setText(song.comment());
            holder.sum_line.setText("");
            holder.title.setText("");
            holder.sum_comment.setText("");
            holder.costSeparator.setVisibility(View.INVISIBLE);
        }

        MemberBaseTableRow to_member = t_members.getMemberById(song.to_member());
        holder.to_member.setText((to_member!=null)?to_member.name:"");
        holder.to_member.setTextColor((to_member!=null)?to_member.color: Color.BLACK);
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
        ImageView have_foto;
        TextView labelHeader;
        View costSeparator;

        ViewHolder(View convertView) {
            super(convertView);
            title = convertView.findViewById(R.id.sum_title); // title
            to_member = convertView.findViewById(R.id.to_member);
            sum_sum = convertView.findViewById(R.id.sum_sum);
            sum_line = convertView.findViewById(R.id.sum_line);
            sum_comment = convertView.findViewById(R.id.sum_comment);
            have_foto = convertView.findViewById(R.id.sum_havefoto);
            labelHeader = convertView.findViewById(R.id.labelHeader);
            costSeparator =  convertView.findViewById(R.id.costSeparator);
        }
    }

    private static void commitEditSum(final EditText input, final ShortCost item, final RecyclerAdapterCostList adapter, DialogInterface dialog, final RecyclerView listView, boolean toDefault)
    {
        double editSum = Help.StringToDouble(String.valueOf(input.getText()));

        double sumGroup = 0;

        List<ShortCost> costGroup = new ArrayList<>();

        if (toDefault) {
            item.setChange(false);
            costGroup.add(item);
        }
        else
        {
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


        for (ShortCost c:costGroup) {
            c.sum = sumGroup/costGroup.size();
        }

        dialog.cancel();
        listView.setAdapter(null);
        listView.setAdapter(adapter);
    }


}


