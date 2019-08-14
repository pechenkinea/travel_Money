package com.pechenkin.travelmoney.cost.group;

import android.view.View;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;

public class OneItemGroup extends GroupCost {

    private Cost cost;

    OneItemGroup(Cost cost) {
        this.cost = cost;
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setBackgroundResource(R.drawable.background_main_layout_list_view);  //добавляем анимацию клика

        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);

        MemberTableRow member = t_members.getMemberById(this.cost.getMember());
        MemberTableRow toMember = t_members.getMemberById(this.cost.getToMember());

        String dateText = "";
        if (this.cost.getDate() != null) {
            dateText = Help.dateToDateTimeStr(this.cost.getDate());
        }
        String comment = dateText + "  " + this.cost.getComment();
        holder.setComment(comment);

        holder.getTitle().setText(member.name);


        holder.getTo_member_one().setText(toMember.name);

        holder.photoImage(this.cost.getImageDir());


        if (cost.isActive()) {
            holder.getSum_group_sum().setText(Help.doubleToString(this.cost.getSum()));
            holder.getTitle().setTextColor(member.color);
            holder.getTo_member_one().setTextColor(toMember.color);

            if (cost.isRepayment()) {
                int color = MainActivity.INSTANCE.getResources().getColor(R.color.colorPrimary);
                holder.getSum_group_sum().setTextColor(color);
            }

        } else {
            holder.getSum_group_sum().setText("0");

            holder.getTitle().setTextColor(DISABLE_COLOR);
            holder.getSum_line().setColorFilter(DISABLE_COLOR);
            holder.getTo_member_one().setTextColor(DISABLE_COLOR);
            holder.getComment().setTextColor(DISABLE_COLOR);

            holder.getSum_group_sum().setTextColor(DISABLE_COLOR);
            holder.getHave_photo().setColorFilter(DISABLE_COLOR);
        }
    }

    @Override
    public boolean onLongClick() {
        if (this.cost.isActive()) {
            t_costs.disable_cost(cost.getId());
            cost.setActive(0);
        } else {
            cost.setActive(1);
            t_costs.enable_cost(cost.getId());
        }
        return true;
    }
}
