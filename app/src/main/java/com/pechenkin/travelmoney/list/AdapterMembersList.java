package com.pechenkin.travelmoney.list;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.page.member.EditMemderPage;
import com.pechenkin.travelmoney.page.PageOpenner;
import com.pechenkin.travelmoney.page.PageParam;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AdapterMembersList extends BaseAdapter {

    private CostMemberBaseTableRow[] data;
    private static LayoutInflater inflater = null;
    private  boolean showEditButton = false;
    private  double sum = 0f;
    private  boolean showSum = false;


    public AdapterMembersList(Activity a, CostMemberBaseTableRow[] dataList, boolean showEditButton)
    {
        data=dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showEditButton = showEditButton;
    }

    public AdapterMembersList(Activity a, CostMemberBaseTableRow[] dataList, double sum)
    {
        data=dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sum = sum;
        showSum = true;
    }
 
    public int getCount() {
        return data.length;
    }
 

 
    public CostMemberBaseTableRow getItem(int position) {
        try {
            return data[position];
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public long getItemId(int position) {
        try {
            return data[position].getMemberRow().id;
        }
        catch (Exception ex)
        {
            return -1;
        }
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
                holder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MemberBaseTableRow item;
                        try {
                            View parentRow = (View) v.getParent();
                            ListView listView = (ListView) parentRow.getParent();
                            int position = listView.getPositionForView(parentRow);
                            item = getItem(position).getMemberRow();
                        } catch (Exception ex) {
                            Help.alert(ex.getMessage());
                            return;
                        }

                        if (item != null) {
                            PageOpenner.INSTANCE.open(EditMemderPage.class, new PageParam.BuildingPageParam().setId(item.id).getParam());
                        }
                    }
                });
            }
            holder.memberSumText = convertView.findViewById(R.id.memberSumText);

            holder.memberSumText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final  CostMemberBaseTableRow item;
                    final ListView listView;
                    try {
                        View parentRow = (View) v.getParent();
                        listView = (ListView) parentRow.getParent();
                        int position = listView.getPositionForView(parentRow);
                        item = getItem(position);
                    } catch (Exception ex) {
                        Help.alert(ex.getMessage());
                        return;
                    }


                    if (item != null) {

                        final EditText input = new EditText(MainActivity.INSTANCE);
                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        input.setText(((TextView)v).getText());

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT );
                        input.setLayoutParams(lp);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                        builder	.setTitle("")
                                .setCancelable(false)
                                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //((TextView) v).setText(input.getText());
                                        item.setSum(Help.StringToDouble(String.valueOf(input.getText())));
                                        item.setChange(true);
                                        dialog.cancel();
                                        listView.invalidateViews();
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
                                        item.setChange(false);
                                        dialog.cancel();
                                        listView.invalidateViews();
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
                                    item.setSum(Help.StringToDouble(String.valueOf(input.getText())));
                                    item.setChange(true);
                                    alert.cancel();
                                    listView.invalidateViews();
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

            convertView.setTag(holder);
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


        MemberBaseTableRow row = data[position].getMemberRow();
        if (row != null) {
            holder.name.setText(row.name);
            holder.name.setTextColor(row.color);


            final ListView lv = (ListView) parent;
            SparseBooleanArray sbArray = lv.getCheckedItemPositions();
            Boolean checked = sbArray.get(position, false);
            holder.memberSumText.setVisibility(View.INVISIBLE);
            if(checked)
            {
                holder.check.setVisibility(View.VISIBLE);

                if (showSum)
                {
                    if (data[position].isChange())
                    {
                        holder.memberSumText.setText(Html.fromHtml("<b>" + Help.DoubleToString(data[position].getSum()) + "</b> "));
                        holder.memberSumText.setVisibility(View.VISIBLE);
                    }
                    else {
                        int selectedCount = 0;
                        double distributionSum = sum;
                        for (int i = 0; i < sbArray.size(); i++) {
                            int key = sbArray.keyAt(i);

                            if (sbArray.get(key)) {
                                if (data[key].isChange())
                                {
                                    distributionSum = distributionSum - data[key].getSum();
                                }
                                else {
                                    selectedCount++;
                                }
                            }
                        }
                        if (selectedCount > 0) {
                            double sumOne = distributionSum / selectedCount;
                            holder.memberSumText.setText(Help.DoubleToString((sumOne > 0)? sumOne : 0));
                            data[position].setSum((sumOne > 0)? sumOne : 0);

                            holder.memberSumText.setVisibility(View.VISIBLE);

                        }
                    }
                }

            }
            else
            {
                holder.check.setVisibility(View.INVISIBLE);
            }

        }



        return convertView;
    }
    
    static class ViewHolder 
    {
        TextView name;
        ImageButton check;
        ImageButton editButton;
        TextView memberSumText;
    }
}
