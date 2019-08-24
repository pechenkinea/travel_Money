package com.pechenkin.travelmoney.transaction.list;

import android.view.View;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;


// TODO наверно можно будет отказаться от этого класса  и объединить его с TotalItem
public class OneItemShort extends TransactionListItem {


    public OneItemShort(Transaction transaction) {
        super(transaction);
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        String sum = Help.kopToTextRub(this.transaction.getSum());


        holder.getSum_group_sum().setText(sum);


        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);

        if (this.transaction.getCreditItems().size() > 0) {
            Member member = this.transaction.getCreditItems().get(0).getMember();
            holder.getTitle().setText(member.getName());
            holder.getTitle().setTextColor(member.getColor());
        }

        if (this.transaction.getDebitItems().size() > 0) {
            Member to_member = this.transaction.getDebitItems().get(0).getMember();
            holder.getTo_member_one().setText(to_member.getName());
            holder.getTo_member_one().setTextColor(to_member.getColor());
        }

    }


    @Override
    public boolean isClicked() {
        return false;
    }
}
