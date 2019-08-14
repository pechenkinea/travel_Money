package com.pechenkin.travelmoney.cost;

import android.view.View;
import android.widget.LinearLayout;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.Repayment;


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

        holder.getMainLayout().setBackgroundResource(R.drawable.edit_button_background);  //добавляем анимацию клика

        LinearLayout ml = holder.getMainLayout();

        int padding6 = Help.dpToPx(8);
        ml.setPadding(ml.getPaddingLeft(), padding6, ml.getPaddingRight(), padding6);

        holder.getMainLayout().setOnClickListener(view -> {
            PageParam param = new PageParam.BuildingPageParam()
                    .setId(getMember())
                    .setToMemberId(getToMember())
                    .setSum(getSum())
                    .getParam();

            PageOpener.INSTANCE.open(Repayment.class, param);
        });

        holder.getCostSeparator().setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean isClicked() {
        return true;
    }

}
