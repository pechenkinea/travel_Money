package com.pechenkin.travelmoney.transaction.list;

import android.view.View;
import android.widget.LinearLayout;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.transaction.draft.DaftTransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;


/**
 * Для того, что бы сделать кликабельными участников в итогах
 */
public class TotalItem extends OneItemShort {




    public TotalItem(Member member, Member toMember, int sum) {

        super(null);

        this.transaction = new DraftTransaction()
                .addTransactionItem(new DaftTransactionItem(member, 0, sum))
                .addTransactionItem(new DaftTransactionItem(toMember, sum, 0));


    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {
        super.render(holder);

        holder.getMainLayout().setBackgroundResource(R.drawable.edit_button_background);  //добавляем анимацию клика

        LinearLayout ml = holder.getMainLayout();

        int padding6 = Help.dpToPx(8);
        ml.setPadding(ml.getPaddingLeft(), padding6, ml.getPaddingRight(), padding6);

        holder.getMainLayout().setOnClickListener(view -> {
            Help.message("Не реализовано");
            //TODO  открывать страницу возврата долга
            /*PageParam param = new PageParam.BuildingPageParam()
                    .setMember(getMember())
                    .setToMember(getToMember())
                    .setSum(getSum())
                    .getParam();

            PageOpener.INSTANCE.open(Repayment.class, param);*/
        });

        holder.getCostSeparator().setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean isClicked() {
        return true;
    }

}
