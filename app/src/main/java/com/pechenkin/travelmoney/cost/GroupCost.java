package com.pechenkin.travelmoney.cost;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.MemberIcons;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Для агрегации информации по нескольким проводкам.
 * Что бы можно было группировать
 */
public class GroupCost implements CostListItem {

    private static int to_member_text_length;
    static {
        to_member_text_length = Integer.parseInt(t_settings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));
        if (to_member_text_length < 4) {
            to_member_text_length = 4;
        }
    }

    /**
     * группирует операции по дате и комментарию
     *
     * @param costs отсортированный по дате массив трат
     */
    public static GroupCost[] group(Cost[] costs) {
        List<GroupCost> groupCostList = new ArrayList<>();
        String lastKey = "";

        for (Cost cost : costs) {
            String key = cost.getDate().getTime() + cost.getComment();
            if (key.equals(lastKey)) {
                try {
                    groupCostList.get(groupCostList.size() - 1).addCost(cost);
                } catch (Exception ex) {
                    Help.alert(ex.getMessage());
                    return null;
                }
            } else {
                groupCostList.add(new GroupCost(cost));
                lastKey = key;
            }
        }

        return groupCostList.toArray(new GroupCost[0]);
    }

    private double sum = 0;
    private final String comment;
    private final List<Cost> costs;
    private final Date date;
    private final String image_dir;

    private GroupCost(Cost cost) {
        this.costs = new ArrayList<>();
        this.comment = cost.getComment();
        this.date = cost.getDate();
        this.image_dir = cost.getImageDir();

        if (cost.isActive() != 0) {
            this.sum = cost.getSum();
        }

        this.costs.add(cost);

    }

    private void addCost(Cost cost) throws Exception {

        if (this.date.getTime() != cost.getDate().getTime() || !this.comment.equals(cost.getComment())) {
            throw new Exception("В группу пробует добавится проводка, которая к ней не относится");
        }

        if (cost.isActive() != 0) {
            this.sum += cost.getSum();
        }
        this.costs.add(cost);
    }

    private void updateSum() {
        this.sum = 0;
        for (Cost cost : costs) {
            if (cost.isActive() != 0) {
                this.sum += cost.getSum();
            }
        }
    }


    public List<Cost> getCosts() {
        return costs;
    }




    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getSum_group_sum().setText(Help.DoubleToString(this.sum));

        List<Cost> costs = getCosts();

        if (costs.size() > 0) {

            MemberBaseTableRow member = t_members.getMemberById(costs.get(0).getMember());

            if (this.sum == 0) {
                holder.getTitle().setTextColor(DISABLE_COLOR);
                holder.getSum_line().setTextColor(DISABLE_COLOR);
                holder.getComment().setTextColor(DISABLE_COLOR);

                holder.getSum_group_sum().setTextColor(DISABLE_COLOR);
                holder.getHave_photo().setColorFilter(DISABLE_COLOR);

            } else {
                holder.getTitle().setTextColor(member.color);
            }


            String dateText = "";
            if (this.date!= null) {
                dateText = Help.dateToDateTimeStr(this.date);
            }
            String comment = dateText + "  " + this.comment;
            holder.setComment(comment);


            holder.getTitle().setText(member.name);

            StringBuilder to_memberText = new StringBuilder();
            StringBuilder sumText = new StringBuilder();


            for (int i = 0; i < costs.size(); i++) {

                Cost costInGroup = costs.get(i);
                MemberBaseTableRow to_member = t_members.getMemberById(costInGroup.getToMember());

                int to_memberColor = to_member.color;

                String s = Help.DoubleToString(costInGroup.getSum());
                if (costInGroup.isActive() != 0) {
                    sumText.append(s);
                } else {
                    sumText.append("<font color='").append(DISABLE_COLOR_STR).append("'>").append(s).append("</font>");
                    to_memberColor = DISABLE_COLOR;
                }

                String to_memberName = to_member.name;
                if (to_memberName.length() > to_member_text_length) {
                    to_memberName = to_memberName.substring(0, to_member_text_length - 3).trim() + "...";
                }

                String strColor = String.format("#%06X", 0xFFFFFF & to_memberColor);
                String to_memberLine = "<font color='" + strColor + "'>" + to_memberName + "</font>";

                to_memberText.append(to_memberLine);

                if (i < costs.size() - 1) {
                    to_memberText.append("<br>");
                    sumText.append("<br>");
                }


                // Иконки человечков
                if (i < 5) {
                    ImageView memberIcon = new ImageView(MainActivity.INSTANCE);

                    memberIcon.setImageDrawable(MemberIcons.getIconById(to_member.icon));
                    memberIcon.setColorFilter(to_memberColor);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 4, 0);
                    memberIcon.setLayoutParams(lp);

                    holder.getMember_icons_layout().addView(memberIcon);

                } else if (i == 5) { //Если в поле "кому" много участников всех не надо показывать. просто добавляем цифру сколько не влезло
                    TextView moreMembers = new TextView(MainActivity.INSTANCE);
                    String moreMembersCount = "+" + (costs.size() - i);
                    moreMembers.setText(moreMembersCount);
                    holder.getMember_icons_layout().addView(moreMembers);

                    if (this.sum == 0) {
                        moreMembers.setTextColor(DISABLE_COLOR);
                    }
                }

                // На случай, если только один участник в блоке "Кому"
                holder.getTo_member_one().setText(to_memberName);
                holder.getTo_member_one().setTextColor(to_memberColor);
            }


            if (costs.size() == 1) {
                holder.getTo_member_one().setVisibility(View.VISIBLE);
                holder.getMember_icons_layout().setVisibility(View.GONE);
            }
            else {

                holder.getMainLayout().setBackgroundResource(R.drawable.background_main_layout_list_view);  //добавляем анимацию клика
                holder.setListenerOpenAdditionalInfo();

            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.getTo_member().setText(Html.fromHtml(to_memberText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
                holder.getSum_sum().setText(Html.fromHtml(sumText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
            } else {
                holder.getTo_member().setText(Html.fromHtml(to_memberText.toString()), TextView.BufferType.SPANNABLE);
                holder.getSum_sum().setText(Html.fromHtml(sumText.toString()), TextView.BufferType.SPANNABLE);
            }


            holder.photoImage(image_dir);
        }


    }

    @Override
    public boolean isClicked() {
        return costs.size() > 1;
    }

    @Override
    public boolean onLongClick() {
        if (costs.size() > 0) {
            long statusFirst = costs.get(0).isActive();
            if (statusFirst == 1) {
                for (Cost cost : costs) {
                    t_costs.disable_cost(cost.getId());
                    cost.setActive(0);
                }
            } else {
                for (Cost cost : costs) {
                    cost.setActive(1);
                    t_costs.enable_cost(cost.getId());
                }
            }
            updateSum();
            return true;
        }
        else {
            return false;
        }
    }
}
