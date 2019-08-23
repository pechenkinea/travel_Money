package com.pechenkin.travelmoney.cost.group;

import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.MemberIcons;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.t_settings;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;

import java.util.Date;

public class ManyItemsGroup extends GroupCost {

    private static int to_member_text_length;

    static {
        to_member_text_length = Integer.parseInt(t_settings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));
        if (to_member_text_length < 4) {
            to_member_text_length = 4;
        }
    }

    private final Cost[] costs;

    private int sum = 0;
    private final String comment;
    private final Date date;
    private final String image_dir;


    private void updateSum() {
        this.sum = 0;
        for (Cost cost : costs) {
            if (cost.isActive()) {
                this.sum += cost.getSum();
            }
        }
    }

    ManyItemsGroup(Cost[] costs) {

        if (costs.length < 2) {
            throw new RuntimeException("ManyItemsGroup предполагает работу с колическтвом трат > 1");
        }

        this.costs = costs;

        this.comment = costs[0].getComment();
        this.date = costs[0].getDate();
        this.image_dir = costs[0].getImageDir();

        for (Cost cost : costs) {

            if (this.date.getTime() != cost.getDate().getTime() || !this.comment.equals(cost.getComment())) {
                throw new RuntimeException("В группу пробует добавится проводка, которая к ней не относится");
            }

            if (cost.isActive()) {
                this.sum += cost.getSum();
            }
        }

    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setBackgroundResource(R.drawable.background_main_layout_list_view);  //добавляем анимацию клика
        holder.setListenerOpenAdditionalInfo();

        holder.getSum_group_sum().setText(Help.kopToTextRub(this.sum));
        Member member = this.costs[0].getMember();

        String dateText = "";
        if (this.date != null) {
            dateText = Help.dateToDateTimeStr(this.date);
        }
        String comment = dateText + "  " + this.comment;
        holder.setComment(comment);

        holder.getTitle().setText(member.getName());

        StringBuilder to_memberText = new StringBuilder();
        StringBuilder sumText = new StringBuilder();

        for (int i = 0; i < this.costs.length; i++) {

            Cost costInGroup = this.costs[i];
            Member to_member = costInGroup.getToMember();

            int to_memberColor = to_member.getColor();

            String s = Help.kopToTextRub(costInGroup.getSum());
            if (costInGroup.isActive()) {
                sumText.append(s);
            } else {
                sumText.append("<font color='").append(DISABLE_COLOR_STR).append("'>").append(s).append("</font>");
                to_memberColor = DISABLE_COLOR;
            }

            String to_memberName = to_member.getName();
            if (to_memberName.length() > to_member_text_length) {
                to_memberName = to_memberName.substring(0, to_member_text_length - 3).trim() + "...";
            }

            String strColor = String.format("#%06X", 0xFFFFFF & to_memberColor);
            String to_memberLine = "<font color='" + strColor + "'>" + to_memberName + "</font>";

            to_memberText.append(to_memberLine);

            if (i < this.costs.length - 1) {
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
                String moreMembersCount = "+" + (costs.length - i);
                moreMembers.setText(moreMembersCount);
                holder.getMember_icons_layout().addView(moreMembers);

                if (this.sum == 0) {
                    moreMembers.setTextColor(DISABLE_COLOR);
                }
            }


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.getTo_member().setText(Html.fromHtml(to_memberText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
            holder.getSum_sum().setText(Html.fromHtml(sumText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
        } else {
            holder.getTo_member().setText(Html.fromHtml(to_memberText.toString()), TextView.BufferType.SPANNABLE);
            holder.getSum_sum().setText(Html.fromHtml(sumText.toString()), TextView.BufferType.SPANNABLE);
        }

        holder.photoImage(image_dir);


        if (this.sum == 0) {
            holder.getTitle().setTextColor(DISABLE_COLOR);
            holder.getSum_line().setColorFilter(DISABLE_COLOR);
            holder.getComment().setTextColor(DISABLE_COLOR);

            holder.getSum_group_sum().setTextColor(DISABLE_COLOR);
            holder.getHave_photo().setColorFilter(DISABLE_COLOR);
        } else {
            holder.getTitle().setTextColor(member.getColor());
        }

    }




    @Override
    public boolean onLongClick() {
        if (this.costs[0].isActive()) {
            for (Cost cost : this.costs) {
                cost.setActive(false);
            }
        } else {
            for (Cost cost : this.costs) {
                cost.setActive(true);
            }
        }
        updateSum();
        return true;
    }
}
