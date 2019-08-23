package com.pechenkin.travelmoney.cost;

import android.view.View;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;

/**
 * Created by pechenkin on 06.04.2018.
 */

public class OnlySumCostItem implements CostListItem {

    private int sum;

    public OnlySumCostItem(int sum) {
        this.sum = sum;
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {
        String sum = Help.kopToTextRub(this.sum);
        holder.getSum_line().setVisibility(View.INVISIBLE);
        holder.getSum_group_sum().setText(sum);
    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean onLongClick() {
        return false;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }
}
