package com.pechenkin.travelmoney.diagram;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.master.MasterCostInfo;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Рисует круговую диаграмму с отображением кто сколько потратил
 * Если открыта страница только для просмотра то диаграмма не кликабельна
 */
public class TotalItemDiagram implements CostListItem, Diagram {

    private double sum;
    private Total.MemberSum[] total;
    private boolean isAnimated = false;
    private PieChart diagram = null;
    private TextView textView = null;
    private boolean readOnly;

    public TotalItemDiagram(double sum, Total.MemberSum[] total, boolean readOnly) {
        this.sum = sum;
        this.readOnly = readOnly;

        Arrays.sort(total, (t1, t2) -> {

            MemberTableRow t1Member = t_members.getMemberById(t1.getMemberId());
            MemberTableRow t2Member = t_members.getMemberById(t2.getMemberId());

            return Integer.compare(t1Member.color, t2Member.color);
        });

        this.total = total;


    }

    private void createDiagram() {

        if (diagram != null)
            return;

        diagram = new PieChart(MainActivity.INSTANCE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(180));

        diagram.setLayoutParams(lp);


        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();

        int[] pieColors = new int[this.total.length];

        int i = 0;
        for (Total.MemberSum c : this.total) {
            long memberId = c.getMemberId();
            MemberTableRow member = t_members.getMemberById(memberId);
            NoOfEmp.add(new PieEntry((float) c.getSumIn(), member.name, memberId));
            pieColors[i++] = member.color;
        }

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        dataSet.setSliceSpace(2); //Отступы между группами
        dataSet.setColors(pieColors); // цвета зон


        //вынос названий за границы
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setValueLinePart2Length(0.6f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        dataSet.setValueTextSize(12);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);

        diagram.setData(data);

        diagram.setEntryLabelColor(Color.BLACK); // цвет имен участников
        diagram.setEntryLabelTextSize(16);

        diagram.getDescription().setEnabled(false);
        diagram.getLegend().setEnabled(false);
        diagram.setRotationEnabled(false); //отключает вращение
        diagram.setMaxHighlightDistance(1);

        if (this.readOnly) {
            diagram.setTouchEnabled(false);
        } else {
            diagram.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(final Entry e, Highlight h) {

                    Object memberId = e.getData();
                    if (memberId instanceof Long){
                        PageParam param = new PageParam.BuildingPageParam()
                                .setId((Long) memberId)
                                .setBackPage(MainPage.class)
                                .getParam();
                        PageOpener.INSTANCE.open(MasterCostInfo.class, param);
                    }

                }

                @Override
                public void onNothingSelected() {
                }
            });
        }

    }

    //Поверх диаграммы добавляем TextView, что бы перекрыть центр и не давать туда кликать.
    private void createTextView() {
        if (textView != null) {
            return;
        }

        textView = new TextView(MainActivity.INSTANCE);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(lp2);

        textView.setBackgroundResource(R.drawable.background_pie_center);

        String text = "Всего\nпотрачено\n" + Help.doubleToString(this.sum);
        textView.setText(text);
        textView.setOnClickListener(null); // перехват клика
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);

    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setVisibility(View.GONE);
        holder.getDiagram().setVisibility(View.VISIBLE);

        createDiagram();
        if(diagram.getParent() != null) {
            // была ошибка. нигрывалась так: надо прокрутить список всех операций вниз,
            // потом пометить трату как неактивную и через кнопку прокрутки вернуться вверх
            // ниже с textView аналогичная ситуация
            ((ViewGroup) diagram.getParent()).removeView(diagram);
        }
        holder.getDiagram().addView(this.diagram);


        createTextView();  //важно добавлять после диаграммы
        if(textView.getParent() != null) {
            ((ViewGroup)textView.getParent()).removeView(textView);
        }
        holder.getDiagram().addView(this.textView);


        if (!isAnimated) {
            isAnimated = true;
            diagram.animateXY(300, 300);
        } else {
            diagram.invalidate();
        }

    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean onLongClick() {
        return false;
    }

    @Override
    public PieChart getDiagram() {
        createDiagram();
        return diagram;
    }

}
