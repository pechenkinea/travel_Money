package com.pechenkin.travelmoney.bd.table.row;

import android.database.Cursor;
import android.view.View;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.Namespace;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.CostListViewHolder;

import java.util.Date;


/**
 * Created by pechenkin on 04.04.2018.
 * Трата
 */

public class CostBaseTableRow extends BaseTableRow implements Cost, CostListItem {

    private final String comment;
    private final String image_dir;

    private final Date date;

    private final long member;
    private final long to_member;
    //public final int currency;
    private long  active;
    //public final int trip;

    private final double sum;


    public CostBaseTableRow(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.sum = getDoubleColumnValue(Namespace.FIELD_SUM, c);
        this.image_dir = getStringColumnValue(Namespace.FIELD_IMAGE_DIR, c);
        this.date = getDateColumnValue(Namespace.FIELD_DATE, c);

        this.member = getLongColumnValue(Namespace.FIELD_MEMBER, c);
        this.to_member = getLongColumnValue(Namespace.FIELD_TO_MEMBER, c);
        //currency = getLongColumnValue(Namespace.FIELD_CURRENCY, c);
        this.active = getLongColumnValue(Namespace.FIELD_ACTIVE, c);
        //trip = getLongColumnValue(Namespace.FIELD_TRIP, c);
    }


    @Override
    public long getId() {
        return id;
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
        return active;
    }

    @Override
    public void setActive(int i) {
        active = i;
    }

    @Override
    public String getImageDir() {
        return image_dir;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void render(CostListViewHolder holder) {

        String sum = Help.DoubleToString(getSum());
        holder.getSum_group_sum().setText(sum);

        holder.disableAdditionalInfo();
        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);


        holder.photoImage(getImageDir());

        MemberBaseTableRow member = t_members.getMemberById(getMember());
        if (member != null) {
            holder.getTitle().setText(member.name);
            holder.getTitle().setTextColor(member.color);
        }

        MemberBaseTableRow to_member = t_members.getMemberById(getToMember());
        if (to_member != null){
            holder.getTo_member_one().setText(to_member.name);
            holder.getTo_member_one().setTextColor(to_member.color);
        }

        holder.photoImage(getImageDir());


        if (isActive() == 0) {
            holder.getTitle().setTextColor(DISABLE_COLOR);
            holder.getSum_line().setTextColor(DISABLE_COLOR);
            holder.getComment().setTextColor(DISABLE_COLOR);
            holder.getSum_group_sum().setTextColor(DISABLE_COLOR);
            holder.getTo_member_one().setTextColor(DISABLE_COLOR);
            holder.getHave_photo().setColorFilter(DISABLE_COLOR);
        }


    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean onLongClick() {
        if (isActive() == 1) {
            t_costs.disable_cost(getId());
            setActive(0);
        } else {
            setActive(1);
            t_costs.enable_cost(getId());
        }
        return true;
    }
}
