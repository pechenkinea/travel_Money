package com.pechenkin.travelmoney.diagram.impl;

import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.transaction.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.Base;
import com.pechenkin.travelmoney.diagram.DiagramName;

import java.util.ArrayList;
import java.util.List;


/**
 * значения кто сколько потратив в виде одной линии
 */
@DiagramName(name = "LineDiagram")
public class LineDiagram extends Base {

    private boolean isAnimated = false;

    public LineDiagram(int sum, List<Total.MemberSum> total) {
        super(sum, total);
    }


    @NonNull
    @Override
    protected Chart getDiagram() {
        if (this.diagram != null)
            return this.diagram;


        int[] pieColors = new int[this.total.size() * 2];
        LegendEntry[] legendEntries = new LegendEntry[this.total.size()];

        int valuesIndex = 0;
        int legendEntriesIndex = 0;
        float[] values = new float[(this.total.size() * 2) - 1];
        float indent = (float) (this.sum / 100 / 80); // отступы

        for (Total.MemberSum c : this.total) {
            values[valuesIndex] = (float) c.getSumExpense() / 100;

            if (valuesIndex < (this.total.size() * 2) - 2) {
                values[valuesIndex + 1] = indent; // отступ
            }


            Member member = c.getMember();

            legendEntries[legendEntriesIndex] = new LegendEntry();
            legendEntries[legendEntriesIndex].label = member.getName();
            legendEntries[legendEntriesIndex].formColor = member.getColor();
            legendEntriesIndex++;

            pieColors[valuesIndex++] = member.getColor();
            pieColors[valuesIndex++] = MainActivity.INSTANCE.getResources().getColor(android.R.color.transparent); // цвет отступа
        }


        List<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(1, values));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(pieColors); // цвета зон

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == indent) {
                    return "";
                }
                return Help.doubleToString(value);
            }
        });

        dataSet.setValueTextColor(Color.WHITE);


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Help.dpToPx(80));
        lp.setMargins(0, Help.dpToPx(4), 0, 0);

        HorizontalBarChart horizontalBarChart = new HorizontalBarChart(MainActivity.INSTANCE);
        horizontalBarChart.setLayoutParams(lp);

        horizontalBarChart.setDrawValueAboveBar(false); // Делает значения внутри полоски диаграммы
        horizontalBarChart.getXAxis().setEnabled(false);

        horizontalBarChart.getAxisRight().setEnabled(false); // снизу не надо значения по y
        horizontalBarChart.getAxisLeft().setEnabled(false); // сверху не надо значения по y

        horizontalBarChart.getLegend().setCustom(legendEntries);
        horizontalBarChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        horizontalBarChart.getDescription().setEnabled(false);

        BarData data = new BarData(dataSet);
        horizontalBarChart.setData(data);

        return horizontalBarChart;
    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {

        TextView textView = getTotalTextView();
        holder.getDiagram().addView(textView);

        super.render(holder);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) this.diagram.getLayoutParams();
        lp.addRule(RelativeLayout.BELOW, textView.getId());
        lp.setMargins(0, Help.dpToPx(8), 0, 0);

        if (!isAnimated) {
            isAnimated = true;
            this.diagram.animateY(300);
        } else {
            this.diagram.invalidate();
        }
    }

    @Override
    protected Member getMemberByEntryAndHighlight(Entry e, Highlight h) {

        if (e instanceof BarEntry && ((BarEntry) e).getYVals().length > 1) {
            int valueIndex = h.getStackIndex();
            if (valueIndex % 2 == 0) {
                return total.get(valueIndex / 2).getMember();
            }
        }
        return null;
    }
}
