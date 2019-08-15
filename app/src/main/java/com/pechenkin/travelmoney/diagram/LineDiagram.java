package com.pechenkin.travelmoney.diagram;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
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

public class LineDiagram implements Diagram {

    private HorizontalBarChart diagram = null;
    private boolean isAnimated = false;
    private Total.MemberSum[] total;
    private OnDiagramSelectItem onDiagramSelectItem = null;
    private double sum;

    public LineDiagram(double sum, Total.MemberSum[] total) {
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

        diagram = new HorizontalBarChart(MainActivity.INSTANCE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(100));
        diagram.setLayoutParams(lp);

        int[] pieColors = new int[this.total.length * 2];


        String[] xValsLabels = new String[this.total.length * 2];
        int i = 0;

        float[] values = new float[this.total.length * 2];

        float indent = (float) (this.sum / 100);

        for (Total.MemberSum c : this.total) {
            values[i] = (float) c.getSumIn();
            values[i + 1] = indent;


            long memberId = c.getMemberId();
            MemberTableRow member = t_members.getMemberById(memberId);

            xValsLabels[i] = member.name;
            xValsLabels[i + 1] = "";
            pieColors[i++] = member.color;
            pieColors[i++] = Color.WHITE;
        }


        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(i, values));


        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(pieColors); // цвета зон

        dataSet.setStackLabels(xValsLabels);

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == indent) {
                    return "";
                }
                return String.valueOf(value);
            }
        });


        dataSet.setValueTextColor(Color.WHITE);

        diagram.setDrawValueAboveBar(false); // Делает значения внутри полоски диаграммы

        diagram.getAxisLeft().setAxisMinimum(0f); //ость Y, значения начинать с 0
        diagram.getAxisLeft().setAxisMaximum((float) (this.sum + (this.total.length * indent)));

        diagram.getXAxis().setEnabled(false);

        //diagram.getAxisRight().setEnabled(false); // снизу не надо значения по y
        diagram.getAxisLeft().setEnabled(false); // сверху не надо значения по y
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
            this.diagram.animateX(300);
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
