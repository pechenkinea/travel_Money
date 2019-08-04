package com.pechenkin.travelmoney.cost;

import android.view.View;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.CostListViewHolder;

import java.util.Date;

/**
 * Created by pechenkin on 06.04.2018.
 */

public class ShortCost implements Cost, CostListItem {

    static private int numerator = 0;

    private final int id = numerator++;

    private boolean isChange = false;

    public final long member;
    private final long to_member;
    public double sum;
    private String comment = "";
    private int groupId = 0;

    public ShortCost(long member, long to_member, double sum) {
        this.member = member;
        this.to_member = to_member;
        this.sum = sum;
    }

    public ShortCost(long member, long to_member, double sum, String comment) {
        this.member = member;
        this.to_member = to_member;
        this.sum = sum;
        this.comment = comment;
    }

    public ShortCost(long member, long to_member, double sum, String comment, int groupId) {
        this.member = member;
        this.to_member = to_member;
        this.sum = sum;
        this.comment = comment;
        this.groupId = groupId;
    }


    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean value) {
        isChange = value;
    }

    @Override
    public long getId() {
        return -1;
    }

    @Override
    public long getMember() {
        return member;
    }

    @Override
    public long getToMember() {
        return to_member;
    }

    @Override
    public double getSum() {
        return sum;
    }

    @Override
    public long isActive() {
        return -1;
    }

    @Override
    public void setActive(int i) {

    }

    @Override
    public String getImageDir() {
        return "";
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public int getGroupId() {
        return groupId;
    }

    @Override
    public void render(CostListViewHolder holder) {

        holder.getComment().setVisibility(View.GONE);

        String sum = Help.DoubleToString(getSum());
        holder.getSum_group_sum().setText(sum);

        holder.disableAdditionalInfo();
        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);

        MemberBaseTableRow member = t_members.getMemberById(getMember());
        if (member != null) {
            holder.getTitle().setText(member.name);
            holder.getTitle().setTextColor(member.color);
        }

        MemberBaseTableRow to_member = t_members.getMemberById(getToMember());
        if (to_member != null) {
            holder.getTo_member_one().setText(to_member.name);
            holder.getTo_member_one().setTextColor(to_member.color);
        }

    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean onLongClick() {
        return false;
    }


    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof ShortCost && ((ShortCost) obj).id == this.id);
    }
}
