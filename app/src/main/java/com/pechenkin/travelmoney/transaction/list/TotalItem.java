package com.pechenkin.travelmoney.transaction.list;

import android.view.View;
import android.widget.LinearLayout;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.Repayment;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.Help;


/**
 * Для того, что бы сделать кликабельными участников в итогах
 */
public class TotalItem extends TransactionListItem {

    private Member member;
    private Member toMember;
    private int sum;


    public TotalItem(Member member, Member toMember, int sum) {

        super(null);

        this.transaction = new DraftTransaction()
                .addCreditItem(new DraftTransactionItem(member, 0, sum))
                .addDebitItem(new DraftTransactionItem(toMember, sum, 0));

        this.member = member;
        this.toMember = toMember;
        this.sum = sum;


    }

    @Override
    public String toString() {
        return member.getName() + " --> " + toMember.getName() + " " + Help.kopToTextRub(sum);
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {


        holder.getMainLayout().setBackgroundResource(R.drawable.edit_button_background);  //добавляем анимацию клика

        holder.getSum_group_sum().setText(
                Help.kopToTextRub(this.sum)
        );


        holder.getTo_member_one().setVisibility(View.VISIBLE);
        holder.getMember_icons_layout().setVisibility(View.GONE);

        if (this.transaction.getCreditItems().size() > 0) {
            Member member = this.transaction.getCreditItems().First().getMember();
            holder.getTitle().setText(member.getName());
            holder.getTitle().setTextColor(member.getColor());
        }

        if (this.transaction.getDebitItems().size() > 0) {
            Member to_member = this.transaction.getDebitItems().First().getMember();
            holder.getTo_member_one().setText(to_member.getName());
            holder.getTo_member_one().setTextColor(to_member.getColor());
        }


        LinearLayout ml = holder.getMainLayout();

        int padding6 = Help.dpToPx(8);
        ml.setPadding(ml.getPaddingLeft(), padding6, ml.getPaddingRight(), padding6);

        holder.getMainLayout().setOnClickListener(view -> {

            PageParam param = new PageParam();
            param.getDraftTransaction()
                    .setRepayment(true)
                    .addCreditItem(new DraftTransactionItem(member, 0, this.sum))
                    .addDebitItem(new DraftTransactionItem(toMember, this.sum, 0));

            PageOpener.INSTANCE.open(Repayment.class, param);
        });

        holder.getCostSeparator().setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean isClicked() {
        return true;
    }

}
