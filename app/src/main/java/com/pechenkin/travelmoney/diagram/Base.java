package com.pechenkin.travelmoney.diagram;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.cost.adapter.ListItemSummaryViewHolder;
import com.pechenkin.travelmoney.cost.processing.summary.Total;

import java.util.Arrays;

public abstract class Base implements Diagram {

    protected Total.MemberSum[] total;
    protected double sum;
    protected Chart diagram = null;
    private OnDiagramSelect onDiagramSelect = null;

    public Diagram setOnDiagramSelect(OnDiagramSelect onDiagramSelect) {
        this.onDiagramSelect = onDiagramSelect;
        return this;
    }

    private OnDiagramSelectItem onDiagramSelectItem = null;

    @Override
    public void setOnDiagramSelectItem(OnDiagramSelectItem onDiagramSelectItem) {
        this.onDiagramSelectItem = onDiagramSelectItem;
    }

    public Base(double sum, Total.MemberSum[] total) {
        this.sum = sum;
        Arrays.sort(total, (t1, t2) -> Integer.compare(t1.getMember().getColor(), t2.getMember().getColor()));

        this.total = total;
    }

    @NonNull
    protected abstract Chart getDiagram();

    private void createDiagram() {

        diagram = getDiagram();


        diagram.setMaxHighlightDistance(16);
        if (diagram instanceof BarChart) { //отключаем масштабирование, а то на "отжатие" срабатывает событие и падает с NPE
            ((BarChart)diagram).setDragEnabled(false);
            ((BarChart)diagram).setScaleEnabled(false);
        }


        if (this.onDiagramSelectItem == null) {
            diagram.setTouchEnabled(false);
        } else {

            diagram.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(final Entry e, Highlight h) {
                    Member member = getMemberByEntryAndHighlight(e, h);
                    if (member != null){
                        onDiagramSelectItem.doOnSelect(member);
                    }

                }

                @Override
                public void onNothingSelected() {
                }
            });
        }
    }

    protected Member getMemberByEntryAndHighlight(Entry e, Highlight h){

        if (e.getData() instanceof Member ) {
            return (Member) e.getData();
        }
        return null;
    }


    @Override
    public void render(ListItemSummaryViewHolder holder) {
        holder.getMainLayout().setVisibility(View.GONE);
        holder.getDiagram().setVisibility(View.VISIBLE);

        if (this.onDiagramSelect != null) {
            holder.getDiagram().setOnClickListener(view -> this.onDiagramSelect.doOnSelect(this));
        }

        createDiagram();

        if (diagram.getParent() != null) {
            // была ошибка. нигрывалась так: надо прокрутить список всех операций вниз,
            // потом пометить трату как неактивную и через кнопку прокрутки вернуться вверх
            // ниже с textView аналогичная ситуация
            ((ViewGroup) diagram.getParent()).removeView(diagram);
        }

        holder.getDiagram().addView(this.diagram);
    }

    protected TextView getTotalTextView() {

        TextView textView = new TextView(MainActivity.INSTANCE);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp2);

        String textTotal = "Всего потрачено " + Help.doubleToString(this.sum);
        textView.setText(textTotal);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(Color.BLACK);

        return textView;
    }

    @Override
    public boolean isClicked() {
        return onDiagramSelect != null;
    }

    @Override
    public boolean onLongClick() {
        return false;
    }
}
