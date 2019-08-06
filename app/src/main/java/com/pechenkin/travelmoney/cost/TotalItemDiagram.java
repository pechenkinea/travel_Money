package com.pechenkin.travelmoney.cost;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.summry.Summary;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Для того, что бы сделать кликабельными участников в итогах
 */
public class TotalItemDiagram implements CostListItem {

    private double sum;
    private Summary[] total;
    private boolean isAnimated = false;

    public TotalItemDiagram(double sum, Summary[] total) {
        this.sum = sum;

        Arrays.sort(total, (t1, t2) -> {

            MemberBaseTableRow t1Member = t_members.getMemberById(t1.member);
            MemberBaseTableRow t2Member = t_members.getMemberById(t2.member);

            return Integer.compare(t1Member.color, t2Member.color);
        });

        this.total = total;
    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setVisibility(View.GONE);
        holder.getDiagram().setVisibility(View.VISIBLE);

        PieChart pieChart = new PieChart(MainActivity.INSTANCE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(200));

        pieChart.setLayoutParams(lp);

        holder.getDiagram().addView(pieChart);

        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();

        int[] pieColors = new int[this.total.length];

        int i=0;
        for (Summary c : this.total) {
            long memberId = c.member;
            MemberBaseTableRow member = t_members.getMemberById(memberId);
            NoOfEmp.add(new PieEntry((float) c.sumIn, member.name));
            pieColors[i++] = member.color;
        }

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
        //dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(2); //Отступы между группами
        dataSet.setColors(pieColors); // цвета зон

        dataSet.setValueLinePart2Length(0.6f);

        //вынос названий за границы
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        dataSet.setValueTextSize(12);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);

        pieChart.setCenterText("Всего\nпотрачено\n" + Help.doubleToString(this.sum));
        pieChart.setCenterTextSize(16);

        pieChart.setEntryLabelColor(Color.BLACK); // цвет имен участников
        pieChart.setEntryLabelTextSize(16);

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setRotationEnabled(false); //отключает вращение
        pieChart.setClickable(false);

        if (!isAnimated) {
            isAnimated = true;
            pieChart.animateXY(300, 300);
        }
        else {
            pieChart.invalidate();
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
