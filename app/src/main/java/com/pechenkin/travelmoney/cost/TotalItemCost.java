package com.pechenkin.travelmoney.cost;

import android.view.View;

import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;


/**
 * Для того, что бы сделать кликабельными участников в итогах
 */
public class TotalItemCost extends ShortCost {
    public TotalItemCost(long member, long to_member, double sum) {
        super(member, to_member, sum);
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {
        super.render(holder);

        holder.getCostSeparator().setVisibility(View.GONE);
    }
}
