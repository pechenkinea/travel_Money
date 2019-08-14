package com.pechenkin.travelmoney.diagram;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarDiagram implements CostListItem, Diagram {

    private BarChart diagram = null;
    private boolean isAnimated = false;
    private Total.MemberSum[] total;

    public BarDiagram(Total.MemberSum[] total) {

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
        diagram.setLayoutParams(lp);

        int[] pieColors = new int[this.total.length];

        List<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> xVals = new ArrayList<>();
        int i = 0;
        for (Total.MemberSum c : this.total) {
            long memberId = c.getMemberId();
            MemberTableRow member = t_members.getMemberById(memberId);
            entries.add(new BarEntry(i, (float) c.getSumIn(), member.name));
            xVals.add(member.name);
            pieColors[i++] = member.color;
        }


        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setColors(pieColors); // цвета зон


        XAxis xAxis = diagram.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false); //вертикальные линии отключаем
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  xVals.get((int) value);
            }
        });


        diagram.getAxisRight().setEnabled(false);
        diagram.getLegend().setEnabled(false);
        diagram.getDescription().setEnabled(false);


        BarData data = new BarData(dataSet);

        diagram.setTouchEnabled(false);

        diagram.setData(data);
        diagram.setFitBars(true); // make the x-axis fit exactly all bars


    }

    @Override
    public Chart getDiagram() {
        createDiagram();
        return diagram;
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
