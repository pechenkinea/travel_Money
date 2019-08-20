package com.pechenkin.travelmoney.diagram.impl;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.Base;
import com.pechenkin.travelmoney.diagram.DiagramName;

import java.util.ArrayList;
import java.util.List;

/**
 * Рисует диаграмму с отображением кто сколько потратил в виде палок
 */
@DiagramName(name = "BarDiagram")
public class BarDiagram extends Base {

    private boolean isAnimated = false;

    public BarDiagram(double sum, Total.MemberSum[] total) {
        super(sum, total);
    }


    @NonNull
    @Override
    protected Chart getDiagram() {
        if (this.diagram != null)
            return this.diagram;


        BarChart diagramBarChart = new BarChart(MainActivity.INSTANCE);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(180));

        diagramBarChart.setLayoutParams(lp);


        int[] pieColors = new int[this.total.length];

        List<BarEntry> entries = new ArrayList<>();
        LegendEntry[] legendEntries = new LegendEntry[this.total.length];
        int i = 0;
        for (Total.MemberSum c : this.total) {
            Member member = c.getMember();
            entries.add(new BarEntry(i, (float) c.getSumExpense(), member));

            legendEntries[i] = new LegendEntry();
            legendEntries[i].label = member.getName();
            legendEntries[i].formColor = member.getColor();

            pieColors[i++] = member.getColor();
        }


        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setColors(pieColors); // цвета зон


        //Пустые значения по оси Х дают отступ
        diagramBarChart.getXAxis().setEnabled(false);

        /*
        XAxis xAxis = diagramBarChart.getXAxis();
        xAxis.setDrawGridLines(false); //вертикальные линии отключаем
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-45); //наклон подписей

        //делаем сетку по оси Х с шагом 1
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        */

        diagramBarChart.getAxisLeft().setAxisMinimum(0f); //ость Y, значения начинать с 0
        diagramBarChart.getAxisRight().setEnabled(false); // справа не надо значения по y
        diagramBarChart.getDescription().setEnabled(false);

        diagramBarChart.getLegend().setCustom(legendEntries);
        diagramBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


        BarData data = new BarData(dataSet);

        diagramBarChart.setData(data);
        diagramBarChart.setFitBars(true); // make the x-axis fit exactly all bars

        diagramBarChart.setScaleEnabled(false);

        return diagramBarChart;
    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {

        TextView textView = getTotalTextView();
        holder.getDiagram().addView(textView);

        super.render(holder);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.diagram.getLayoutParams();
        lp.addRule(RelativeLayout.BELOW, textView.getId());
        lp.setMargins(0, Help.dpToPx(16), 0, 0);

        if (!isAnimated) {
            isAnimated = true;
            this.diagram.animateY(300);
        } else {
            this.diagram.invalidate();
        }


    }

}
