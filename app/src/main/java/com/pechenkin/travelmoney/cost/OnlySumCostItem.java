package com.pechenkin.travelmoney.cost;

import android.text.Html;
import android.view.View;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;

import java.util.Date;

/**
 * Created by pechenkin on 06.04.2018.
 */

public class OnlySumCostItem implements CostListItem {

    private double sum;

    public OnlySumCostItem(double sum) {
        this.sum = sum;
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {
        String sum = Help.DoubleToString(this.sum);
        holder.getSum_line().setText("");
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

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }
}
