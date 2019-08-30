package com.pechenkin.travelmoney.transaction.list;

import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.TableSettings;
import com.pechenkin.travelmoney.transaction.Transaction;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.MemberIcons;

public class ManyItems extends TransactionListItem {

    private static int to_member_text_length;

    static {
        to_member_text_length = Integer.parseInt(TableSettings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));
        if (to_member_text_length < 4) {
            to_member_text_length = 4;
        }
    }

    ManyItems(Transaction transaction) {
        super(transaction);
    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setBackgroundResource(R.drawable.background_main_layout_list_view);  //добавляем анимацию клика
        holder.setListenerOpenAdditionalInfo();

        holder.getSum_group_sum().setText(Help.kopToTextRub(this.transaction.getSum()));

        Member member = this.transaction.getCreditItems().First().getMember();

        String dateText = Help.dateToDateTimeStr(this.transaction.getDate());
        String comment = dateText + "  " + this.transaction.getComment();
        holder.setComment(comment);

        holder.getTitle().setText(member.getName());

        StringBuilder to_memberText = new StringBuilder();
        StringBuilder sumText = new StringBuilder();

        for (int i = 0; i < this.transaction.getDebitItems().size(); i++) {

            TransactionItem costInGroup = this.transaction.getDebitItems().get(i);
            Member to_member = costInGroup.getMember();

            int to_memberColor = to_member.getColor();

            if (!this.transaction.isActive()) {
                to_memberColor = DISABLE_COLOR;
            }

            String s = Help.kopToTextRub(costInGroup.getDebit());

            sumText.append(s);

            String to_memberName = to_member.getName();
            if (to_memberName.length() > to_member_text_length) {
                to_memberName = to_memberName.substring(0, to_member_text_length - 3).trim() + "...";
            }


            String strColor = String.format("#%06X", 0xFFFFFF & to_memberColor);
            String to_memberLine = "<font color='" + strColor + "'>" + to_memberName + "</font>";

            to_memberText.append(to_memberLine);

            if (i < this.transaction.getDebitItems().size() - 1) {
                to_memberText.append("<br>");
                sumText.append("<br>");
            }


            // Иконки человечков
            if (i < 5) {
                ImageView memberIcon = new ImageView(MainActivity.INSTANCE);

                memberIcon.setImageDrawable(MemberIcons.getIconById(to_member.getIcon()));
                memberIcon.setColorFilter(to_memberColor);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 4, 0);
                lp.gravity = Gravity.CENTER_VERTICAL;

                memberIcon.setLayoutParams(lp);


                holder.getMember_icons_layout().addView(memberIcon);

            } else if (i == 5) { //Если в поле "кому" много участников всех не надо показывать. просто добавляем цифру сколько не влезло
                TextView moreMembers = new TextView(MainActivity.INSTANCE);
                String moreMembersCount = "+" + (this.transaction.getDebitItems().size() - i);
                moreMembers.setText(moreMembersCount);
                if (!this.transaction.isActive()) {
                    moreMembers.setTextColor(DISABLE_COLOR);
                }
                holder.getMember_icons_layout().addView(moreMembers);
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.getTo_member().setText(Html.fromHtml(to_memberText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
            holder.getSum_sum().setText(Html.fromHtml(sumText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
        } else {
            holder.getTo_member().setText(Html.fromHtml(to_memberText.toString()), TextView.BufferType.SPANNABLE);
            holder.getSum_sum().setText(Html.fromHtml(sumText.toString()), TextView.BufferType.SPANNABLE);
        }

        holder.photoImage(this.transaction.getImageUrl());


        if (!this.transaction.isActive()) {
            holder.getTitle().setTextColor(DISABLE_COLOR);
            holder.getSum_line().setColorFilter(DISABLE_COLOR);
            holder.getComment().setTextColor(DISABLE_COLOR);

            holder.getSum_group_sum().setTextColor(DISABLE_COLOR);
            holder.getHave_photo().setColorFilter(DISABLE_COLOR);
        } else {
            holder.getTitle().setTextColor(member.getColor());
        }

    }


}
