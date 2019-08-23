package com.pechenkin.travelmoney.cost.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pechenkin.travelmoney.Division;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.cost.OnlySumCostItem;
import com.pechenkin.travelmoney.cost.ShortCost;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Адаптер для страницы редактирования трат при добавлении голосом
 */
public class RecyclerAdapterCostList extends RecyclerView.Adapter {


    private final List<CostListItem> data;
    private boolean isCanEditAllSum = false;
    private int totalSum;
    private static LayoutInflater inflater = null;
    private final RecyclerView listView;


    public RecyclerAdapterCostList(Context a, List<CostListItem> data, RecyclerView listView) {

        this.listView = listView;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;

        int sum = 0;
        Set<Integer> groups = new HashSet<>();

        for (CostListItem c : data) {
            if (c instanceof ShortCost) {
                sum += ((ShortCost) c).sum;
                if (((ShortCost) c).getGroupId() > 0) {
                    groups.add(((ShortCost) c).getGroupId());
                }
            }
        }

        this.totalSum = sum;
        //В последнюю строку добавляем сумму всех трат
        this.data.add(new OnlySumCostItem(this.totalSum));

        // если после распознавания фразы пришла только одна группа трат. т.е. это на фраза вроде "Я за Грина и Свету 140 а за Влада 30" или "Я за всех 350 магазин и за Грина 140"
        // тогда можно будет редактировать общую сумму и она будет распределяться по всем
        if (groups.size() == 1) {
            isCanEditAllSum = true;
        }

    }


    public CostListItem getItem(int position) {
        try {
            return data.get(position);
        } catch (Exception ex) {
            return null;
        }
    }

    public List<CostListItem> getData() {
        return data;
    }

    /**
     * Обновление общий суммы трат
     */
    private void updateTotalSum(){
        int allSum = 0;
        for (CostListItem c : data) {
            if (c instanceof ShortCost) {
                if (((ShortCost) c).member != null) {
                    allSum += ((ShortCost) c).getSum();
                }
            }
        }

        this.totalSum = allSum;
        ((OnlySumCostItem)data.get(data.size() - 1)).setSum(this.totalSum);


        listView.setAdapter(null);
        listView.setAdapter(this);


        View refresh_button = MainActivity.INSTANCE.findViewById(R.id.add_costs_list_refresh_button);
        if (refresh_button != null) {
            refresh_button.setVisibility(View.VISIBLE);
        }
    }

    public void remove(int position) {

        CostListItem item = getItem(position);

        if (item instanceof ShortCost){
            int groupId = ((ShortCost) item).getGroupId();

            int sumGroup = 0;


            if (((ShortCost) item).isChange()) {
                sumGroup = ((ShortCost) item).getSum();
            }

            List<ShortCost> costGroup = new ArrayList<>();

            for (CostListItem c : data) {
                if (c instanceof ShortCost) {
                    if (!((ShortCost) c).isChange()) {
                        if (((ShortCost) c).getGroupId() == groupId) {
                            if (!c.equals(item)) {
                                costGroup.add((ShortCost) c);
                            }

                            sumGroup += ((ShortCost) c).getSum();
                        }
                    }
                }
            }


            data.remove(position);
            notifyItemRemoved(position);

            Division division = new Division(sumGroup, costGroup.size());
            for (ShortCost c : costGroup) {
                c.sum = division.getNext();
            }

            updateTotalSum();

        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item_summary_group, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        CostListItem cost = data.get(h.getAdapterPosition());

        ViewHolder holder = (ViewHolder) h;

        holder.getListItemSummaryViewHolder().toDefaultView();

        cost.render(holder.getListItemSummaryViewHolder());


        if (cost instanceof ShortCost || isCanEditAllSum && cost instanceof OnlySumCostItem){
            holder.getListItemSummaryViewHolder().setEditButtonClickListener(v1 -> {

                View listItem = (View) v1.getParent().getParent();

                CostListItem item = getItem(listView.getChildLayoutPosition(listItem));

                if (item instanceof ShortCost || item instanceof OnlySumCostItem) {

                    final EditText input = new EditText(MainActivity.INSTANCE);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    input.setText(((TextView) listItem.findViewById(R.id.sum_group_sum)).getText());

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);



                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                    builder.setTitle("")
                            .setCancelable(false)
                            .setPositiveButton("Ок", (dialog, which) -> commitEditSum(input, item, dialog, false))
                            .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

                    if (item instanceof ShortCost ) {
                        builder.setNeutralButton("По умолчанию", (dialog, which) -> commitEditSum(input, item, dialog, true));
                    }


                    final AlertDialog alert = builder.create();
                    alert.setView(input);
                    if (alert.getWindow() != null)
                        alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                    input.setOnEditorActionListener((v11, actionId, event) -> {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            commitEditSum(input, item, alert, false);
                            return true;
                        }
                        return false;
                    });


                    alert.show();

                    Help.setActiveEditText(input, true);

                }

            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Обработка редактирования суммы одной строки
     */
    private void commitEditSum(final EditText input, final CostListItem item, DialogInterface dialog, boolean toDefault) {

        int editSum = Help.textRubToIntKop(String.valueOf(input.getText()));
        int sumGroup = 0;

        List<ShortCost> costGroup = new ArrayList<>();
        int groupId = 1;


        if (item instanceof OnlySumCostItem) {
            sumGroup = editSum;

            for (CostListItem c : data) {
                if (c instanceof ShortCost) {
                    if (((ShortCost) c).isChange()) {
                        sumGroup = sumGroup - ((ShortCost) c).sum;
                        continue;
                    }

                    if (((ShortCost) c).getGroupId() == groupId) {
                        costGroup.add((ShortCost) c);
                    }
                }
            }

        } else  if (item instanceof ShortCost) {
            if (toDefault) {
                ((ShortCost) item).setChange(false);
                costGroup.add((ShortCost) item);
            } else {
                sumGroup = ((ShortCost) item).getSum();
                ((ShortCost) item).sum = editSum;
                ((ShortCost) item).setChange(true);
                sumGroup = sumGroup - editSum;
            }
            groupId = ((ShortCost) item).getGroupId();

            for (CostListItem c : data) {
                if (c instanceof ShortCost) {
                    if (((ShortCost) c).isChange())
                        continue;

                    if (((ShortCost) c).getGroupId() == groupId) {
                        if (!c.equals(item))
                            costGroup.add((ShortCost) c);
                        sumGroup += ((ShortCost) c).getSum();
                    }
                }
            }
        }

        if (sumGroup < 0)
            sumGroup = 0;


        Division division = new Division(sumGroup, costGroup.size());
        for (ShortCost c : costGroup) {
            c.sum = division.getNext();
        }

        updateTotalSum();

        dialog.cancel();

    }


    private static class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemSummaryViewHolder listItemSummaryViewHolder;

        ViewHolder(View convertView) {
            super(convertView);
            listItemSummaryViewHolder = new ListItemSummaryViewHolder(convertView);
        }

        private ListItemSummaryViewHolder getListItemSummaryViewHolder() {
            return listItemSummaryViewHolder;
        }
    }


}


