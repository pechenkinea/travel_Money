package com.pechenkin.travelmoney.transaction.list;

import android.view.View;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;

public class OneItem extends TransactionListItem {


    public OneItem(Transaction transaction) {
        super(transaction);
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setBackgroundResource(R.drawable.background_main_layout_list_view);  //добавляем анимацию клика

        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);

        Member member = this.transaction.getCreditItems().First().getMember();
        Member toMember = this.transaction.getDebitItems().First().getMember();

        String dateText = "";
        dateText = Help.dateToDateTimeStr(this.transaction.getDate());

        String comment = dateText + "  " + this.transaction.getComment();
        holder.setComment(comment);

        holder.getTitle().setText(member.getName());


        holder.getTo_member_one().setText(toMember.getName());

        holder.photoImage(this.transaction.getImageUrl());


        if (transaction.isActive()) {
            holder.getSum_group_sum().setText(Help.kopToTextRub(this.transaction.getSum()));
            holder.getTitle().setTextColor(member.getColor());
            holder.getTo_member_one().setTextColor(toMember.getColor());

            if (transaction.isRepayment()) {
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


}
