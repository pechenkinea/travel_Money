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

        /*holder.getTitle().setBackgroundResource(R.drawable.edit_button_background);
        holder.getTitle().setOnClickListener(view -> {
            PageParam param = new PageParam.BuildingPageParam().setId(getMember()).getParam();
            PageOpener.INSTANCE.open(MasterCostInfo.class, param);
        });

        holder.getTo_member_one().setBackgroundResource(R.drawable.edit_button_background);
        holder.getTo_member_one().setOnClickListener(view -> {
            PageParam param = new PageParam.BuildingPageParam().setId(getToMember()).getParam();
            PageOpener.INSTANCE.open(MasterCostInfo.class, param);
        });*/

        holder.getCostSeparator().setVisibility(View.INVISIBLE);
    }
}
