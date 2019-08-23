package com.pechenkin.travelmoney.diagram.impl;

import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.HorizontalBarHighlighter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.utils.MPPointD;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.Base;
import com.pechenkin.travelmoney.diagram.DiagramName;

import java.util.ArrayList;
import java.util.List;

@DiagramName(name = "DebitCreditDiagram")
public class DebitCreditDiagram extends Base {
    private boolean isAnimated = false;

    public DebitCreditDiagram(int sum, List<Total.MemberSum> total) {
        super(sum, total);
    }

    @NonNull
    @Override
    protected Chart getDiagram() {
        if (this.diagram != null)
            return this.diagram;


        HorizontalBarChart horizontalBarChart = new HorizontalBarChart(MainActivity.INSTANCE);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(180));

        horizontalBarChart.setLayoutParams(lp);

        int[] pieColors = new int[this.total.size()];

        List<BarEntry> entries = new ArrayList<>();
        LegendEntry[] legendEntries = new LegendEntry[this.total.size()];
        int i = 0;
        int legendIndex = this.total.size() - 1;  //В горизонтальной диаграмме рисуется зеркально и легенду надо писать в обратном порядке
        for (Total.MemberSum c : this.total) {
            Member member = c.getMember();

            double totalSum = c.getSumPay() - c.getSumExpense();

            entries.add(new BarEntry(i, (float) totalSum, member));

            legendEntries[legendIndex] = new LegendEntry();
            legendEntries[legendIndex].label = member.getName();
            legendEntries[legendIndex].formColor = member.getColor();
            legendIndex--;

            pieColors[i] = member.getColor();

            i++;
        }

        horizontalBarChart.getXAxis().setGranularity(1f);
        horizontalBarChart.getXAxis().setGranularityEnabled(true);

        horizontalBarChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return Help.doubleToString(entries.get((int) value).getY());
            }
        });


        horizontalBarChart.getAxisRight().setEnabled(false); // снизу не надо значения по y


        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(pieColors); // цвета зон

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });

        //dataSet.setValueTextColor(Color.WHITE);
        //dataSet.setDrawValues(true);

        //horizontalBarChart.getAxisLeft().setAxisMinimum(0f); //ость Y, значения начинать с 0
        horizontalBarChart.getDescription().setEnabled(false);

        horizontalBarChart.setDrawValueAboveBar(false); // Делает значения внутри полоски диаграммы

        horizontalBarChart.getLegend().setCustom(legendEntries);
        horizontalBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


        BarData data = new BarData(dataSet);

        horizontalBarChart.setData(data);
        horizontalBarChart.setFitBars(true); // make the x-axis fit exactly all bars


        horizontalBarChart.setHighlighter(new MyHighlighter(horizontalBarChart));


        return horizontalBarChart;

    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {
        super.render(holder);

        if (!isAnimated) {
            isAnimated = true;
            this.diagram.animateY(300);
        } else {
            this.diagram.invalidate();
        }
    }







    private static class MyHighlighter extends HorizontalBarHighlighter {

        private MyHighlighter(BarDataProvider chart) {
            super(chart);
        }

        @SuppressWarnings("all")
        @Override
        protected Highlight getHighlightForX(float xVal, float x, float y) {

            Highlight result = super.getHighlightForX(xVal, x, y);
            if (result != null) {

                MPPointD pos = getValsForTouch(y, x);

                //если нажали на колонку с другой стороны от нуля то не надо выделять строку. это нажатие на пустое место
                if (result.getY() * pos.y > 0) {
                    return null;
                }

            }
            return result;
        }
    }

}
