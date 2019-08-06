package com.pechenkin.travelmoney.cost.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.ListAnimation;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

import java.io.File;

public class ListItemSummaryViewHolder {

    private final TextView title;
    private final TextView to_member;
    private final TextView to_member_one;
    private final TextView sum_group_sum;
    private final TextView sum_sum;
    private final TextView sum_line;
    private final TextView comment;
    private final AppCompatImageView have_photo;
    private final AppCompatImageView listEditButton;
    private final TextView labelHeader;
    private final View mainLayout;
    private final LinearLayout member_icons_layout;
    private final View more_information_layout;
    private final View costSeparator;
    private final FrameLayout diagram;


    ListItemSummaryViewHolder(View convertView) {
        this.title = convertView.findViewById(R.id.sum_title);
        this.to_member = convertView.findViewById(R.id.toMembers);
        this.to_member_one = convertView.findViewById(R.id.to_member_one);
        this.sum_group_sum = convertView.findViewById(R.id.sum_group_sum);
        this.sum_sum = convertView.findViewById(R.id.sum_sum);
        this.sum_line = convertView.findViewById(R.id.sum_line);
        this.comment = convertView.findViewById(R.id.comment);
        this.have_photo = convertView.findViewById(R.id.sum_have_photo);
        this.listEditButton = convertView.findViewById(R.id.listEditButton);
        this.labelHeader = convertView.findViewById(R.id.labelHeader);
        this.mainLayout = convertView.findViewById(R.id.mainLayout);
        this.member_icons_layout = convertView.findViewById(R.id.member_icons_layout);
        this.more_information_layout = convertView.findViewById(R.id.more_information_layout);
        this.costSeparator = convertView.findViewById(R.id.costSeparator);
        this.diagram = convertView.findViewById(R.id.diagram);

    }

    public void setListenerOpenAdditionalInfo() {
        /*
         * Обработчик на нажатие кнопки открытия дополнительной информации
         */
        this.mainLayout.setOnClickListener(view -> {
            if (this.more_information_layout.getVisibility() == View.GONE) {
                ListAnimation.expand(more_information_layout);
            } else {
                ListAnimation.collapse(more_information_layout);
            }
        });


        this.mainLayout.setOnLongClickListener(view -> false);
    }

    /**
     * Взвращает строку в изначальное положение. а то бывает, чт при прокрутке списка вьюха запоминает параметры от чужой строки
     */
    void toDefaultView() {

        this.title.setText("");
        this.title.setTextColor(Color.BLACK);
        this.title.setBackground(null);
        this.title.setOnClickListener(null);

        this.sum_sum.setText("");
        this.sum_sum.setTextColor(Color.BLACK);

        this.sum_group_sum.setText("");
        this.sum_group_sum.setTextColor(Color.BLACK);

        this.sum_line.setText("-->");
        this.sum_line.setTextColor(Color.BLACK);


        this.comment.setVisibility(View.GONE);
        this.comment.setTextColor(Color.BLACK);

        this.have_photo.setColorFilter(Color.BLACK);
        this.have_photo.setVisibility(View.GONE);
        this.have_photo.setOnClickListener(null);

        this.listEditButton.setColorFilter(Color.BLACK);
        listEditButton.setVisibility(View.GONE);
        this.listEditButton.setOnClickListener(null);

        this.to_member.setTextColor(Color.BLACK);

        this.labelHeader.setVisibility(View.GONE);
        this.more_information_layout.setVisibility(View.GONE);


        this.to_member_one.setVisibility(View.GONE);
        this.to_member_one.setTextColor(Color.BLACK);
        this.to_member_one.setBackground(null);
        this.to_member_one.setOnClickListener(null);

        this.member_icons_layout.removeAllViews(); //очищаем все иконки человечков
        this.member_icons_layout.setVisibility(View.VISIBLE);

        this.mainLayout.setVisibility(View.VISIBLE);
        this.mainLayout.setBackground(null);  //убираем анимацию клика

        this.mainLayout.setOnClickListener(null);

        this.costSeparator.setVisibility(View.VISIBLE);


        this.diagram.setVisibility(View.GONE);
        this.diagram.removeAllViews();
    }

    /**
     * показывает/скрывает иконку фотографии в зависимости от наличия ссылки
     */
    public void photoImage(String dir) {
        if (dir.length() > 0) {
            this.have_photo.setVisibility(View.VISIBLE);

            this.have_photo.setOnClickListener(view -> {

                String realPath = dir;
                if (dir.contains(".provider")) {
                    //костыль, т.к. раньше в БД хранилось значение уже после работы FileProvider
                    String badPath = "content://" + MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider/external_files";
                    realPath = dir.replaceFirst(badPath, Environment.getExternalStorageDirectory().getAbsolutePath());
                }

                File file = new File(realPath);
                if (!file.exists()) {
                    Help.alertError("Файл не найден. " + realPath);
                    return;
                }

                Uri uri = FileProvider.getUriForFile(
                        MainActivity.INSTANCE,
                        MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(uri, "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                MainActivity.INSTANCE.startActivity(intent);

            });
        } else {
            this.have_photo.setVisibility(View.GONE);
        }
    }

    public void setEditButtonClickListener(View.OnClickListener clickListener){
        this.listEditButton.setVisibility(View.VISIBLE);
        this.listEditButton.setOnClickListener(clickListener);
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getTo_member() {
        return to_member;
    }

    public TextView getTo_member_one() {
        return to_member_one;
    }

    public TextView getSum_group_sum() {
        return sum_group_sum;
    }

    public TextView getSum_sum() {
        return sum_sum;
    }

    public TextView getSum_line() {
        return sum_line;
    }

    public TextView getComment() {
        return comment;
    }

    public void setComment(String text) {
        if (text.trim().length() > 0) {
            this.comment.setVisibility(View.VISIBLE);
            this.comment.setText(text);
        }
    }

    public AppCompatImageView getHave_photo() {
        return have_photo;
    }

    void setHeader(String text) {
        this.labelHeader.setVisibility(View.VISIBLE);
        this.mainLayout.setVisibility(View.GONE);
        this.costSeparator.setVisibility(View.INVISIBLE);
        this.labelHeader.setText(text);
    }

    public View getCostSeparator() {
        return costSeparator;
    }

    public View getMainLayout() {
        return mainLayout;
    }

    public LinearLayout getMember_icons_layout() {
        return member_icons_layout;
    }

    public FrameLayout getDiagram() {
        return diagram;
    }

}
