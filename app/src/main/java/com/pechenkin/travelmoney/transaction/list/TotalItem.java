package com.pechenkin.travelmoney.transaction.list;

import android.view.View;
import android.widget.LinearLayout;

import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.Repayment;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;


/**
 * Для того, что бы сделать кликабельными участников в итогах
 */
public class TotalItem extends OneItemShort {

    private Member member;
    private Member toMember;
    private int sum;


    public TotalItem(Member member, Member toMember, int sum) {

        super(null);

        this.transaction = new DraftTransaction()
                .addTransactionItem(new DraftTransactionItem(member, 0, sum))
                .addTransactionItem(new DraftTransactionItem(toMember, sum, 0));

        this.member = member;
        this.toMember = toMember;
        this.sum = sum;


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

            PageParam param = new PageParam();
            param.getDraftTransaction()
                    .setRepayment(true)
                    .addTransactionItem(new DraftTransactionItem(member, 0, sum))
                    .addTransactionItem(new DraftTransactionItem(toMember, sum, 0));

            PageOpener.INSTANCE.open(Repayment.class, param);
        });

        holder.getCostSeparator().setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean isClicked() {
        return true;
    }

}
