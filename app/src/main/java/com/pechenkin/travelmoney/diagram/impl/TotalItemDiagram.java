package com.pechenkin.travelmoney.diagram.impl;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.transaction.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.Base;
import com.pechenkin.travelmoney.diagram.DiagramName;

import java.util.ArrayList;
import java.util.List;


/**
 * Рисует круговую диаграмму с отображением кто сколько потратил
 */
@DiagramName(name = "TotalItemDiagram")
public class TotalItemDiagram extends Base {

    private boolean isAnimated = false;

    public TotalItemDiagram(int sum, List<Total.MemberSum> total) {
        super(sum, total);
    }


    @NonNull
    @Override
    protected Chart getDiagram() {
        if (this.diagram != null)
            return this.diagram;


        PieChart diagramPieChart = new PieChart(MainActivity.INSTANCE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(180));

        diagramPieChart.setLayoutParams(lp);


        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();

        int[] pieColors = new int[this.total.size()];

        int i = 0;
        for (Total.MemberSum c : this.total) {
            Member member = c.getMember();
            NoOfEmp.add(new PieEntry((float) c.getSumExpense(), member.getName(), member));
            pieColors[i++] = member.getColor();
        }

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        dataSet.setSliceSpace(2); //Отступы между группами
        dataSet.setColors(pieColors); // цвета зон


        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Help.kopToTextRub((int) value);
            }
        });

        //вынос названий за границы
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setValueLinePart2Length(0.6f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        dataSet.setValueTextSize(12);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);

        diagramPieChart.setData(data);

        diagramPieChart.setEntryLabelColor(Color.BLACK); // цвет имен участников
        diagramPieChart.setEntryLabelTextSize(16);

        diagramPieChart.getDescription().setEnabled(false);
        diagramPieChart.getLegend().setEnabled(false);
        diagramPieChart.setRotationEnabled(false); //отключает вращение

        diagramPieChart.setMaxHighlightDistance(1);

        return  diagramPieChart;

    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {

        super.render(holder);

        TextView textView = new TextView(MainActivity.INSTANCE);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(lp2);

        textView.setBackgroundResource(R.drawable.background_pie_center);

        String text = "Всего\nпотрачено\n" + Help.kopToTextRub(this.sum);
        textView.setText(text);
        textView.setOnClickListener(null); // перехват клика
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        if (textView.getParent() != null) {
            ((ViewGroup) textView.getParent()).removeView(textView);
        }
        holder.getDiagram().addView(textView); // Важно добавлять после диаграммы


        if (!isAnimated) {
            isAnimated = true;
            diagram.animateXY(300, 300);
        } else {
            diagram.invalidate();
        }

    }

}
