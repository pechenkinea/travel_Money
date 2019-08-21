package com.pechenkin.travelmoney.cost.group;

import android.view.View;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.t_costs;
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

        Member member = this.cost.getMember();
        Member toMember = this.cost.getToMember();

        String dateText = "";
        if (this.cost.getDate() != null) {
            dateText = Help.dateToDateTimeStr(this.cost.getDate());
        }
        String comment = dateText + "  " + this.cost.getComment();
        holder.setComment(comment);

        holder.getTitle().setText(member.getName());


        holder.getTo_member_one().setText(toMember.getName());

        holder.photoImage(this.cost.getImageDir());


        if (cost.isActive()) {
            holder.getSum_group_sum().setText(Help.doubleToString(this.cost.getSum()));
            holder.getTitle().setTextColor(member.getColor());
            holder.getTo_member_one().setTextColor(toMember.getColor());

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
            cost.setActive(false);
        } else {
            cost.setActive(true);
        }
        return true;
    }
}
