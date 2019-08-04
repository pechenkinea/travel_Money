package com.pechenkin.travelmoney.cost.adapter;

import android.view.View;

import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.CostListViewHolder;

/**
 * Created by pechenkin on 06.04.2018.
 * Для итогов и дополнительных строк листа операций
 */

public class LabelItem implements CostListItem {


    private final String label;

    public LabelItem(String label)
    {
        this.label = label;
    }


    @Override
    public void render(CostListViewHolder holder) {

        holder.disableAdditionalInfo();
        holder.getMainLayout().setVisibility(View.GONE);
        holder.getComment().setVisibility(View.GONE);

        holder.getLabelHeader().setVisibility(View.VISIBLE);
        holder.getLabelHeader().setText(label);

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
