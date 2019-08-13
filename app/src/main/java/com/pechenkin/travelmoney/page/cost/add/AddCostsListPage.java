package com.pechenkin.travelmoney.page.cost.add;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.OnlySumCostItem;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.cost.adapter.CostListItem;
import com.pechenkin.travelmoney.cost.adapter.LabelItem;
import com.pechenkin.travelmoney.cost.adapter.RecyclerAdapterCostList;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pechenkin on 19.04.2018.
 * Страница для добаваления трат при голосовом вводе
 */
public class AddCostsListPage extends BasePage {


    @Override
    protected int getPageId() {
        return R.layout.add_cost_list;
    }

    @Override
    protected String getTitleHeader() {
        return "Добавить траты" + "(" + t_trips.getActiveTrip().name + ")";
    }

    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class);
    }


    @Override
    public void addEvents() {

        FloatingActionButton add_costs_list_refresh_button = MainActivity.INSTANCE.findViewById(R.id.add_costs_list_refresh_button);
        add_costs_list_refresh_button.setOnClickListener(view -> refreshForm());

        FloatingActionButton add_cost_list_commit = MainActivity.INSTANCE.findViewById(R.id.add_cost_list_commit);
        add_cost_list_commit.setOnClickListener(v -> {

            RecyclerView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.list_add_costs);
            RecyclerAdapterCostList adapter = (RecyclerAdapterCostList) listViewCosts.getAdapter();

            TextInputEditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);
            String comment = getTextInputEditText(add_cost_comment);

            if (comment.length() == 0) {
                Help.message(MainActivity.INSTANCE.getString(R.string.errorFillDescription));
                Help.setActiveEditText(R.id.add_cost_comment);
                return;
            }

            if (adapter == null) {
                Help.alert("adapter is null");
                return;
            }

            List<CostListItem> costs = adapter.getData();

            boolean added = false;
            Date addCostDate = new Date();
            for (CostListItem c : costs) {
                if (c instanceof ShortCost) {
                    if (((ShortCost) c).getMember() > -1 && ((ShortCost) c).getSum() > 0) {
                        t_costs.add(((ShortCost) c).getMember(), ((ShortCost) c).getToMember(), comment, ((ShortCost) c).getSum(), "", t_trips.getActiveTrip().id, addCostDate, false);
                        added = true;
                    }
                }
            }

            if (added) {
                Help.message(MainActivity.INSTANCE.getString(R.string.messageAddCost));
            }

            PageOpener.INSTANCE.open(MainPage.class);
        });


    }


    private void refreshForm() {
        EditText add_costs_text = MainActivity.INSTANCE.findViewById(R.id.add_costs_text);
        String text = add_costs_text.getText().toString();
        TextInputEditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);

        if (text.length() > 0) {
            CostCreator cc = new CostCreator(text, getTextInputEditText(add_cost_comment));
            PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
            PageOpener.INSTANCE.open(AddCostsListPage.class, param);
        }

        MainActivity.INSTANCE.findViewById(R.id.add_costs_list_refresh_button).setVisibility(View.INVISIBLE);
    }

    @Override
    protected boolean fillFields() {
        if (t_trips.getActiveTrip() == null) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }

        if (!hasParam() || getParam().getCostCreator() == null)
            return false;

        EditText text = MainActivity.INSTANCE.findViewById(R.id.add_costs_text);
        text.setText(getParam().getCostCreator().getText());


        List<CostListItem> finalList = new ArrayList<>();
        if (getParam().getCostCreator().hasCosts()) {
            finalList.add(new LabelItem("Проверьте кто кому сколько дал"));

            ShortCost[] costs = getParam().getCostCreator().getCosts();
            finalList.addAll(Arrays.asList(costs));


        } else {
            finalList.add(new LabelItem("Не удалось разобрать"));
        }

        TextInputEditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);
        add_cost_comment.setText(getParam().getCostCreator().getComment());


        final RecyclerView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.list_add_costs);
        listViewCosts.setHasFixedSize(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.INSTANCE);
        listViewCosts.setLayoutManager(mLayoutManager);


        final RecyclerAdapterCostList adapter = new RecyclerAdapterCostList(MainActivity.INSTANCE, finalList, listViewCosts);


        final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Определяет можно ли удалить строку свайпом. Если вернуть 0 то строку двигать нельзя
             */
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                CostListItem item = adapter.getItem(viewHolder.getAdapterPosition());
                if (item instanceof ShortCost) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }
                return 0;
            }

            /**
             * При сдвиге траты в сторону
             * Не просто удаляет трату из списка а перераспределяет сумму на другие траты, сумма которых не редактировалась руками
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.remove(viewHolder.getAdapterPosition());
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(listViewCosts);


        listViewCosts.setAdapter(adapter);
        return true;

    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }


}
