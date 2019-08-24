package com.pechenkin.travelmoney.transaction.list;

import android.view.View;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.Help;

public class DraftTransactionItemListItem implements CostListItem {

    private DraftTransactionItem draftTransactionItem;
    private Member member;

    public DraftTransactionItemListItem(DraftTransactionItem draftTransactionItem, Member member) {
        this.draftTransactionItem = draftTransactionItem;
        this.member = member;
    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {

        String sum = Help.kopToTextRub(this.draftTransactionItem.getDebit());
        holder.getSum_group_sum().setText(sum);

        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);

        holder.getTitle().setText(member.getName());
        holder.getTitle().setTextColor(member.getColor());

        holder.getTo_member_one().setText(draftTransactionItem.getMember().getName());
        holder.getTo_member_one().setTextColor(draftTransactionItem.getMember().getColor());
    }


    public DraftTransactionItem getDraftTransactionItem() {
        return draftTransactionItem;
    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean onLongClick() {
        return false;
    }
}
