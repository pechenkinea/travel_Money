package com.pechenkin.travelmoney.cost;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pechenkin.travelmoney.Help;
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

        //dataSet.setSelectionShift(1f);
        //dataSet.setValueLinePart1Length(1f);
        //dataSet.setValueLinePart2Length(0.9f);

        //вынос названий за границы
        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.WHITE);

        holder.getPiechart().setData(data);

        holder.getPiechart().setCenterText("Всего\n" + Help.doubleToString(this.sum));
        holder.getPiechart().setCenterTextSize(16);

        holder.getPiechart().setEntryLabelColor(Color.BLACK); // цвет имен участников

        holder.getPiechart().getDescription().setEnabled(false);
        holder.getPiechart().getLegend().setEnabled(false);
        holder.getPiechart().setRotationEnabled(false); //отключает вращение

        if (!isAnimated) {
            isAnimated = true;
            holder.getPiechart().animateXY(500, 500);
        }
        else {
            holder.getPiechart().invalidate();
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
