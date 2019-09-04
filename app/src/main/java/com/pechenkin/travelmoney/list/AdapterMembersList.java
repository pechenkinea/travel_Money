package com.pechenkin.travelmoney.list;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.member.EditMemberPage;
import com.pechenkin.travelmoney.utils.MemberIcons;

import java.util.List;

public class AdapterMembersList extends BaseAdapter {

    private final List<Member> data;
    private static LayoutInflater inflater = null;
    private final boolean showEditButton;
    private boolean showCheckBox = true;

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public AdapterMembersList(Activity a, List<Member> dataList, boolean showEditButton) {
        data = dataList;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showEditButton = showEditButton;
    }


    public int getCount() {
        return data.size();
    }


    public Member getItem(int position) {
        try {
            return data.get(position);
        } catch (Exception ex) {
            return null;
        }
    }

    public long getItemId(int position) {
        try {
            return data.get(position).getId();
        } catch (Exception ex) {
            return -1;
        }
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        Member item = data.get(position);


        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (showEditButton) {
            holder.editButton.setVisibility(View.VISIBLE);

            holder.editButton.setOnClickListener(v ->
                    PageOpener.INSTANCE.open(EditMemberPage.class, new PageParam().setMember(item))
            );

        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }

        if (!showCheckBox) {
            holder.check.setVisibility(View.GONE);
        }


        holder.name.setText(item.getName());

        holder.name.setTextColor(item.getColor());

        holder.icon.setVisibility(View.VISIBLE);
        holder.icon.setImageDrawable(MemberIcons.getIconById(item.getIcon()));
        holder.icon.setColorFilter(item.getColor());


        final ListView lv = (ListView) parent;
        SparseBooleanArray sbArray = lv.getCheckedItemPositions();
        boolean checked = sbArray.get(position, false);
        holder.memberSumText.setVisibility(View.INVISIBLE);
        if (checked) {
            holder.check.setImageResource(R.drawable.ic_check_on_24);
        } else {
            holder.check.setImageResource(R.drawable.ic_check_off_24);
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
