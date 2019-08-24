package com.pechenkin.travelmoney.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.util.SparseBooleanArray;
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

import androidx.appcompat.widget.AppCompatImageView;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.data.CostMember;
import com.pechenkin.travelmoney.page.member.EditMemberPage;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.MemberIcons;

import java.util.List;

public class AdapterMembersList extends BaseAdapter {

    private final List<CostMember> data;
    private static LayoutInflater inflater = null;
    private boolean showEditButton = false;
    private int sum = 0;
    private boolean showSum = false;
    private boolean showCheckBox = true;

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public AdapterMembersList(Activity a, List<CostMember> dataList, boolean showEditButton) {
        data = dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showEditButton = showEditButton;
    }

    public AdapterMembersList(Activity a, List<CostMember> dataList, int sum) {
        data = dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sum = sum;
        showSum = true;
    }

    public int getCount() {
        return data.size();
    }


    public CostMember getItem(int position) {
        try {
            return data.get(position);
        } catch (Exception ex) {
            return null;
        }
    }

    public long getItemId(int position) {
        try {
            return data.get(position).getMember().getId();
        } catch (Exception ex) {
            return -1;
        }
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        CostMember item = data.get(position);


        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (showEditButton) {
            holder.editButton.setOnClickListener(v ->
                    PageOpener.INSTANCE.open(EditMemberPage.class, new PageParam().setMember(item.getMember()))
            );
        }

        if (showSum) {
            View.OnClickListener editClickListener = v -> {
                final ListView listView;
                try {
                    listView = (ListView) v.getParent().getParent();
                } catch (Exception ex) {
                    Help.alert(ex.getMessage());
                    return;
                }

                final EditText input = new EditText(MainActivity.INSTANCE);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                input.setText(Help.kopToTextRub(item.getSum()));

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                builder.setTitle("")
                        .setCancelable(false)
                        .setPositiveButton("Ок", (dialog, which) -> {
                            //((TextView) v).setText(input.getText());
                            item.setSum(Help.textRubToIntKop(String.valueOf(input.getText())));
                            item.setChange(true);
                            dialog.cancel();
                            listView.invalidateViews();
                        })
                        .setNegativeButton("Отмена",
                                (dialog, id) -> dialog.cancel())
                        .setNeutralButton("По умолчанию", (dialog, which) -> {
                            item.setChange(false);
                            dialog.cancel();
                            listView.invalidateViews();
                        });


                final AlertDialog alert = builder.create();
                alert.setView(input);
                if (alert.getWindow() != null)
                    alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                input.setOnEditorActionListener((v1, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        item.setSum(Help.textRubToIntKop(String.valueOf(input.getText())));
                        item.setChange(true);
                        alert.cancel();
                        listView.invalidateViews();
                        return true;
                    }
                    return false;
                });

                alert.show();

                Help.setActiveEditText(input, true);

            };

            holder.memberSumText.setOnClickListener(editClickListener);
            holder.editButton.setOnClickListener(editClickListener);
        }


        if (showEditButton) {
            holder.editButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }

        if (!showCheckBox) {
            holder.check.setVisibility(View.GONE);
        }


        Member row = item.getMember();
        if (row != null) {
            holder.name.setText(row.getName());

            holder.name.setTextColor(row.getColor());

            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageDrawable(MemberIcons.getIconById(row.getIcon()));
            holder.icon.setColorFilter(row.getColor());


            final ListView lv = (ListView) parent;
            SparseBooleanArray sbArray = lv.getCheckedItemPositions();
            boolean checked = sbArray.get(position, false);
            holder.memberSumText.setVisibility(View.INVISIBLE);
            if (checked) {
                holder.check.setImageResource(R.drawable.ic_check_on_24);

                if (showSum) {
                    holder.memberSumText.setVisibility(View.VISIBLE);
                    holder.editButton.setVisibility(View.VISIBLE);

                    if (data.get(position).isChange()) {
                        holder.memberSumText.setText(Html.fromHtml("<b>" + Help.kopToTextRub(data.get(position).getSum()) + "</b> "));

                    } else {
                        //holder.memberSumText.setText(Help.kopToTextRub(data.get(position).getSum()));

                        int selectedCount = 0;
                        int distributionSum = sum;
                        for (int i = 0; i < sbArray.size(); i++) {
                            int key = sbArray.keyAt(i);

                            if (sbArray.get(key)) {
                                if (data.get(key).isChange()) {
                                    distributionSum = distributionSum - data.get(key).getSum();
                                } else {
                                    selectedCount++;
                                }
                            }
                        }
                        if (selectedCount > 0) {
                            int sumOne = distributionSum / selectedCount; // TODO делить без отстатка

                            holder.memberSumText.setText(Help.kopToTextRub((sumOne > 0) ? sumOne : 0));
                            data.get(position).setSum((sumOne > 0) ? sumOne : 0);


                        }
                    }
                }

            } else {
                holder.check.setImageResource(R.drawable.ic_check_off_24);
            }

        }


        return convertView;
    }

    static class ViewHolder {
        final TextView name;
        final ImageButton check;
        final ImageButton editButton;
        final TextView memberSumText;
        final AppCompatImageView icon;

        ViewHolder(View convertView) {
            this.name = convertView.findViewById(R.id.lm_name);
            this.check = convertView.findViewById(R.id.lm_check);
            this.editButton = convertView.findViewById(R.id.listEditButton);
            this.memberSumText = convertView.findViewById(R.id.memberSumText);
            this.icon = convertView.findViewById(R.id.icon);
        }


    }
}
