package com.pechenkin.travelmoney.cost.adapter;

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
    public void render(ListItemSummaryViewHolder holder) {
        holder.setHeader(label);
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
