package com.pechenkin.travelmoney.diagram;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.master.MasterCostInfo;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Рисует круговую диаграмму с отображением кто сколько потратил
 * Если открыта страница только для просмотра то диаграмма не кликабельна
 */
public class TotalItemDiagram implements CostListItem, Diagram {

    private double sum;
    private Total.MemberSum[] total;
    private boolean isAnimated = false;
    private mPieChart pieChart = null;
    private boolean readOnly;

    public TotalItemDiagram(double sum, Total.MemberSum[] total, boolean readOnly) {
        this.sum = sum;
        this.readOnly = readOnly;

        Arrays.sort(total, (t1, t2) -> {

            MemberBaseTableRow t1Member = t_members.getMemberById(t1.getMemberId());
            MemberBaseTableRow t2Member = t_members.getMemberById(t2.getMemberId());

            return Integer.compare(t1Member.color, t2Member.color);
        });

        this.total = total;


    }

    private void createDiagram() {

        if (pieChart != null)
            return;

        pieChart = new mPieChart(MainActivity.INSTANCE);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                Help.dpToPx(200));

        pieChart.setLayoutParams(lp);


        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();

        int[] pieColors = new int[this.total.length];

        int i = 0;
        for (Total.MemberSum c : this.total) {
            long memberId = c.getMemberId();
            MemberBaseTableRow member = t_members.getMemberById(memberId);
            NoOfEmp.add(new CostPieEntry((float) c.getSumIn(), member.name, memberId));
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

        if (this.readOnly) {
            pieChart.setTouchEnabled(false);
        } else {
            CostPieEntry[] selectedEntry = new CostPieEntry[]{null};

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(final Entry e, Highlight h) {
                    if (e instanceof CostPieEntry) {

                        if (selectedEntry[0] != null && selectedEntry[0].equals(e)) {
                            PageParam param = new PageParam.BuildingPageParam()
                                    .setId(selectedEntry[0].getMemberId())
                                    .setBackPage(MainPage.class)
                                    .getParam();
                            PageOpener.INSTANCE.open(MasterCostInfo.class, param);
                        } else {
                            selectedEntry[0] = (CostPieEntry) e;
                        }

                    }
                }

                @Override
                public void onNothingSelected() {
                }
            });
        }

    }

    @Override
    public void render(ListItemSummaryViewHolder holder) {

        holder.getMainLayout().setVisibility(View.GONE);
        holder.getDiagram().setVisibility(View.VISIBLE);

        createDiagram();

        holder.getDiagram().addView(pieChart);


        if (!isAnimated) {
            isAnimated = true;
            pieChart.animateXY(300, 300);
        } else {
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

    @Override
    public PieChart getDiagram() {
        createDiagram();
        return pieChart;
    }


    static private class mPieChart extends PieChart {

        public mPieChart(Context context) {
            super(context);
        }

        //что бы сторонние клики не сбрасывали выделенную ячейку, что бы можно было обработать повторный клик
        @Override
        public void highlightValue(Highlight high, boolean callListener) {

            if (high == null)
                return;

            Entry e = mData.getEntryForHighlight(high);
            if (e == null) {
                return;
            } else {
                mIndicesToHighlight = new Highlight[]{
                        high
                };
            }

            setLastHighlighted(mIndicesToHighlight);

            if (callListener && mSelectionListener != null) {
                mSelectionListener.onValueSelected(e, high);
            }
            invalidate();
        }
    }

    static private class CostPieEntry extends PieEntry {

        private long memberId;

        CostPieEntry(float value, String label, long memberId) {
            super(value, label);
            this.memberId = memberId;
        }

        long getMemberId() {
            return memberId;
        }
    }
}
