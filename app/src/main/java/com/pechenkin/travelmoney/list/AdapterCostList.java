package com.pechenkin.travelmoney.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.ListAnimation;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.MemberIcons;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.GroupCost;

import java.text.SimpleDateFormat;
import java.util.List;


//TODO переписать это все
public class AdapterCostList extends BaseAdapter {

    private Cost[] data;
    private static LayoutInflater inflater = null;

    private int to_member_text_length = 12;

    public AdapterCostList(Context a, Cost[] data) {
        this.data = data;
        try {
            to_member_text_length = Integer.parseInt(t_settings.INSTANCE.get(NamespaceSettings.TO_MEMBER_TEXT_LENGTH));
            if (to_member_text_length < 4) {
                to_member_text_length = 4;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*public Cost[] getData() {
        return data;
    }*/

    @Override
    public int getCount() {
        return (data != null) ? data.length : 0;
    }

    @Override
    public Cost getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean isEnabled(int position) {
        Cost item = data[position];
        return item != null && (item.id() > 0 || item instanceof GroupCost);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yy HH:mm");

    @SuppressLint({"DefaultLocale", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {

        Cost cost = data[position];

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_summary_group, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.toDefaultView();


        if (cost == null) {
            return convertView;
        }

        String summ = Help.DoubleToString(cost.sum());
        holder.sum_group_sum.setText((cost.sum() != 0) ? summ : "");

        String dateText = "";
        if (cost.date() != null) {
            dateText = df2.format(cost.date());
        }
        String comment = dateText + "  " + cost.comment();
        holder.comment.setText(comment);

        if (cost.comment().length() == 0) {
            holder.comment.setVisibility(View.GONE);
        }


        holder.photoImage(cost.image_dir());


        String colorDisable = "#CCCCCC";
        int colorDisableColor = Color.parseColor(colorDisable);

        if (cost instanceof GroupCost) {

            List<Cost> costs = ((GroupCost) cost).getCosts();

            if (costs.size() > 0) {

                MemberBaseTableRow member = t_members.getMemberById(costs.get(0).member());

                if (cost.sum() == 0) {
                    holder.title.setTextColor(colorDisableColor);
                    holder.sum_line.setTextColor(colorDisableColor);
                    holder.comment.setTextColor(colorDisableColor);

                    holder.sum_group_sum.setText("0");
                    holder.sum_group_sum.setTextColor(colorDisableColor);
                } else {
                    holder.title.setTextColor(member.color);
                }
                holder.title.setText(member.name);

                StringBuilder to_memberText = new StringBuilder();
                StringBuilder sumText = new StringBuilder();


                for (int i = 0; i < costs.size(); i++) {

                    Cost costInGroup = costs.get(i);
                    MemberBaseTableRow to_member = t_members.getMemberById(costInGroup.to_member());

                    int to_memberColor = to_member.color;

                    String s = Help.DoubleToString(costInGroup.sum());
                    if (costInGroup.active() != 0) {
                        sumText.append(s);
                    } else {
                        sumText.append("<font color='").append(colorDisable).append("'>").append(s).append("</font>");
                        to_memberColor = colorDisableColor;
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

                        memberIcon.setImageResource(MemberIcons.getIconById(to_member.icon));
                        memberIcon.setColorFilter(to_memberColor);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 4, 0); //компенсация отступов
                        memberIcon.setLayoutParams(lp);

                        holder.member_icons_layout.addView(memberIcon);

                    } else if (i == 5) { //Если в поле "кому" много участников всех не надо показывать. просто добавляем цифру сколько не влезло
                        TextView moreMembers = new TextView(MainActivity.INSTANCE);
                        moreMembers.setText(String.format("+%d", costs.size() - i));
                        holder.member_icons_layout.addView(moreMembers);
                    }

                    // На случай, если только один участник в блоке "Кому"
                    holder.to_member_one.setText(to_memberName);
                    holder.to_member_one.setTextColor(to_memberColor);
                }


                if (costs.size() == 1) {
                    holder.disableAdditionalInfo();
                    holder.to_member_one.setVisibility(View.VISIBLE);
                    holder.member_icons_layout.setVisibility(View.GONE);
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.to_member.setText(Html.fromHtml(to_memberText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
                    holder.sum_sum.setText(Html.fromHtml(sumText.toString(), Html.FROM_HTML_MODE_COMPACT), TextView.BufferType.SPANNABLE);
                } else {
                    holder.to_member.setText(Html.fromHtml(to_memberText.toString()), TextView.BufferType.SPANNABLE);
                    holder.sum_sum.setText(Html.fromHtml(sumText.toString()), TextView.BufferType.SPANNABLE);
                }
            }

        } else {

            holder.disableAdditionalInfo();
            holder.to_member_one.setVisibility(View.VISIBLE);
            holder.member_icons_layout.setVisibility(View.GONE);

            MemberBaseTableRow member = t_members.getMemberById(cost.member());
            if (member != null) {
                holder.title.setText(member.name);
                holder.title.setTextColor(member.color);
            } else {
                holder.labelHeader.setVisibility(View.VISIBLE);
                holder.mainLayout.setVisibility(View.GONE);

                holder.labelHeader.setText(cost.comment());
                holder.sum_line.setText("");
                holder.title.setText("");
                holder.comment.setText("");
            }

            MemberBaseTableRow to_member = t_members.getMemberById(cost.to_member());
            holder.to_member_one.setText((to_member != null) ? to_member.name : "");
            holder.to_member_one.setTextColor((to_member != null) ? to_member.color : Color.BLACK);


            if (cost.active() == 0) {
                holder.title.setTextColor(colorDisableColor);
                holder.sum_line.setTextColor(colorDisableColor);
                holder.comment.setTextColor(colorDisableColor);
                holder.sum_group_sum.setTextColor(colorDisableColor);
                holder.to_member_one.setTextColor(colorDisableColor);
            }
        }

        return convertView;
    }

    static class ViewHolder {

        TextView title;
        TextView to_member;
        TextView to_member_one;
        TextView sum_group_sum;
        TextView sum_sum;
        TextView sum_line;
        TextView comment;
        AppCompatImageView have_photo;
        TextView labelHeader;
        View mainLayout;
        LinearLayout member_icons_layout;
        View more_information_layout;

        boolean activeAdditionalInfo = true;

        ViewHolder(View convertView) {
            this.title = convertView.findViewById(R.id.sum_title);
            this.to_member = convertView.findViewById(R.id.to_member);
            this.to_member_one = convertView.findViewById(R.id.to_member_one);
            this.sum_group_sum = convertView.findViewById(R.id.sum_group_sum);
            this.sum_sum = convertView.findViewById(R.id.sum_sum);
            this.sum_line = convertView.findViewById(R.id.sum_line);
            this.comment = convertView.findViewById(R.id.comment);
            this.have_photo = convertView.findViewById(R.id.sum_havefoto);
            this.labelHeader = convertView.findViewById(R.id.labelHeader);
            this.mainLayout = convertView.findViewById(R.id.mainLayout);
            this.member_icons_layout = convertView.findViewById(R.id.member_icons_layout);
            this.more_information_layout = convertView.findViewById(R.id.more_information_layout);
            setListener();
        }


        void disableAdditionalInfo() {
            this.mainLayout.setBackground(null);  //убираем анимацию клика
            this.activeAdditionalInfo = false;    //отключаем клик
        }

        private void setListener() {
            /*
             * Обработчик на нажатие кнопки открытия дополнительной информации
             */
            this.mainLayout.setOnClickListener(view -> {
                if (activeAdditionalInfo) {
                    if (this.more_information_layout.getVisibility() == View.GONE) {
                        ListAnimation.expand(more_information_layout);
                    } else {
                        ListAnimation.collapse(more_information_layout);
                    }
                }
            });


            this.mainLayout.setOnLongClickListener(view -> false);
        }

        /**
         * Взвращает строку в изначальное положение. а то бывает, чт при прокрутке списка вьюха запоминает параметры от чужой строки
         */
        void toDefaultView() {

            this.sum_sum.setText("");
            this.sum_group_sum.setText("");
            this.sum_line.setText("-->");

            this.title.setTextColor(Color.BLACK);
            this.sum_line.setTextColor(Color.BLACK);
            this.comment.setTextColor(Color.BLACK);
            this.to_member.setTextColor(Color.BLACK);
            this.sum_sum.setTextColor(Color.BLACK);

            this.labelHeader.setVisibility(View.GONE);
            this.mainLayout.setVisibility(View.VISIBLE);
            this.comment.setVisibility(View.VISIBLE);

            this.more_information_layout.setVisibility(View.GONE);


            this.to_member_one.setVisibility(View.GONE);
            this.member_icons_layout.removeAllViews(); //очищаем все иконки и отрисовываем по новой. на случай когда отменили трату
            this.member_icons_layout.setVisibility(View.VISIBLE);

            this.activeAdditionalInfo = true;
            this.mainLayout.setBackgroundResource(R.drawable.background_main_layout_list_view);  //добавляем анимацию клика

        }

        /**
         * показывает/скрывает иконку фотографии в зависимости от наличия ссылки
         */
        void photoImage(String dir) {
            if (dir.length() > 0) {
                this.have_photo.setVisibility(View.VISIBLE);
            } else {
                this.have_photo.setVisibility(View.GONE);
            }
        }

    }


}


