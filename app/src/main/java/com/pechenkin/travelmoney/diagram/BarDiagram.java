package com.pechenkin.travelmoney.diagram;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarDiagram implements Diagram {

    private BarChart diagram = null;
    private boolean isAnimated = false;
    private double sum;
    private Total.MemberSum[] total;
    private OnDiagramSelectItem onDiagramSelectItem = null;

    public BarDiagram(double sum, Total.MemberSum[] total) {
        this.sum = sum;

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

        diagram = new BarChart(MainActivity.INSTANCE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(180));
        lp.setMargins(0, Help.dpToPx(12), 0, 0);
        diagram.setLayoutParams(lp);


        int[] pieColors = new int[this.total.length];

        List<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> xValsLabels = new ArrayList<>();
        int i = 0;
        for (Total.MemberSum c : this.total) {
            long memberId = c.getMemberId();
            MemberTableRow member = t_members.getMemberById(memberId);
            entries.add(new BarEntry(i, (float) c.getSumIn(), member.id));
            xValsLabels.add(member.name);
            pieColors[i++] = member.color;
        }


        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setColors(pieColors); // цвета зон


        XAxis xAxis = diagram.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false); //вертикальные линии отключаем
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45); //наклон подписей

        //делаем сетку по оси Х с шагом 1
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xValsLabels.get((int) value);
            }
        });

        diagram.getAxisLeft().setAxisMinimum(0f); //ость Y, значения начинать с 0

        diagram.getAxisRight().setEnabled(false); // справа не надо значения по y
        diagram.getLegend().setEnabled(false);
        diagram.getDescription().setEnabled(false);


        BarData data = new BarData(dataSet);

        diagram.setData(data);
        diagram.setFitBars(true); // make the x-axis fit exactly all bars


        if (this.onDiagramSelectItem == null) {
            diagram.setTouchEnabled(false);
        } else {
            diagram.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(final Entry e, Highlight h) {
                    Object memberId = e.getData();
                    if (memberId instanceof Long) {
                        onDiagramSelectItem.doOnSelect((Long) memberId);
                    }
                }

                @Override
                public void onNothingSelected() {
                }
            });
        }
    }


    @Override
    public void setOnDiagramSelectItem(OnDiagramSelectItem onDiagramSelectItem) {
        this.onDiagramSelectItem = onDiagramSelectItem;
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setVisibility(View.GONE);
        holder.getDiagram().setVisibility(View.VISIBLE);

        TextView textView = new TextView(MainActivity.INSTANCE);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp2);

        String textTotal = "Всего потрачено " + Help.doubleToString(this.sum);
        textView.setText(textTotal);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);

        holder.getDiagram().addView(textView);

        createDiagram();

        if (diagram.getParent() != null) {
            // была ошибка. нигрывалась так: надо прокрутить список всех операций вниз,
            // потом пометить трату как неактивную и через кнопку прокрутки вернуться вверх
            // ниже с textView аналогичная ситуация
            ((ViewGroup) diagram.getParent()).removeView(diagram);
        }

        holder.getDiagram().addView(this.diagram);

        if (!isAnimated) {
            isAnimated = true;
            this.diagram.animateY(300);
        } else {
            this.diagram.invalidate();
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
}
