package com.pechenkin.travelmoney.diagram;

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
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

import java.util.ArrayList;
import java.util.List;

public class DebitCreditDiagram extends Base {
    public DebitCreditDiagram(double sum, Total.MemberSum[] total) {
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

        int[] pieColors = new int[this.total.length];

        List<BarEntry> entries = new ArrayList<>();
        LegendEntry[] legendEntries = new LegendEntry[this.total.length];
        int i = 0;
        int legendIndex = this.total.length-1;  //В горизонтальной диаграмме рисуется зеркально и легенду надо писать в обратном порядке
        for (Total.MemberSum c : this.total) {
            long memberId = c.getMemberId();
            MemberTableRow member = t_members.getMemberById(memberId);

            double totalSum = c.getSumOut() - c.getSumIn();

            entries.add(new BarEntry(i, (float) totalSum, member.id));

            legendEntries[legendIndex] = new LegendEntry();
            legendEntries[legendIndex].label = member.name;
            legendEntries[legendIndex].formColor = member.color;
            legendIndex--;

            pieColors[i] = member.color;

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


        return horizontalBarChart;

    }
}
