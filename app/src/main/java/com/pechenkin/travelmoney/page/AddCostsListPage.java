package com.pechenkin.travelmoney.page;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.ShortCost;
import com.pechenkin.travelmoney.list.RecyclerAdapterCostList;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pechenkin on 19.04.2018.]
 * Страница с перечнем участников
 */

public class AddCostsListPage extends BasePage {


    @Override
    protected int getPageId() {
        return R.layout.add_cost_list;
    }

    @Override
    protected String getTitleHeader() {
        return "Добавить траты" +  "(" + t_trips.ActiveTrip.name + ")";
    }

    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MainPage.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void addEvents() {

        Button add_cost_list_commit = MainActivity.INSTANCE.findViewById(R.id.add_cost_list_commit);
        add_cost_list_commit.setOnClickListener(v -> {


            RecyclerView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.list_add_costs);
            RecyclerAdapterCostList adapter = (RecyclerAdapterCostList)listViewCosts.getAdapter();

            EditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);
            String comment = add_cost_comment.getText().toString().trim();

            if (comment.length() == 0)
            {
                Help.message(MainActivity.INSTANCE.getString(R.string.errorFillDescription));
                Help.setActiveEditText(R.id.add_cost_comment);
                return;
            }

            List<ShortCost> costs = adapter.getData();

            boolean added = false;
            Date addCostDate = new Date();
            for(ShortCost c : costs)
            {
                if (c.member() > -1 && c.sum() > 0)
                {
                    t_costs.add(c.member(), c.to_member(), comment, c.sum(), "", t_trips.ActiveTrip.id, addCostDate);
                    added = true;
                }
            }

            if (added)
            {
                Help.message(MainActivity.INSTANCE.getString(R.string.messageAddCost));
            }

            PageOpenner.INSTANCE.open(MainPage.class);
        });



        Button add_costs_list_revert_button = MainActivity.INSTANCE.findViewById(R.id.add_costs_list_revert_button);
        add_costs_list_revert_button.setOnClickListener(view -> clickBackButton());


        Button add_costs_list_refresh_button = MainActivity.INSTANCE.findViewById(R.id.add_costs_list_refresh_button);
        add_costs_list_refresh_button.setOnClickListener(view -> refreshForm());



        EditText add_costs_text = MainActivity.INSTANCE.findViewById(R.id.add_costs_text);
        add_costs_text.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                refreshForm();
                return true;
            }
            return false;
        });

        EditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);
        add_cost_comment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                refreshForm();
                return true;
            }
            return false;
        });

    }


    private void refreshForm()
    {
        EditText add_costs_text = MainActivity.INSTANCE.findViewById(R.id.add_costs_text);
        String text = add_costs_text.getText().toString();
        EditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);

        if (text.length() > 0)
        {
            CostCreator cc = new CostCreator(text, add_cost_comment.getText().toString());
            PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
            PageOpenner.INSTANCE.open(AddCostsListPage.class, param);
        }
    }

    @Override
    protected boolean fillFields() {
        if (t_trips.ActiveTrip == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }

        if (!hasParam() || getParam().getCostCreator() == null )
            return false;

        EditText text = MainActivity.INSTANCE.findViewById(R.id.add_costs_text);
        text.setText(getParam().getCostCreator().getText());


        List<ShortCost> finalList = new ArrayList<>();
        if (getParam().getCostCreator().hasCosts()) {
            finalList.add( new ShortCost(-1, -1, 0f, "↓ Проверьте кто кому сколько дал ↓"));
            finalList.addAll(Arrays.asList(getParam().getCostCreator().getCosts()));
        }
        else
        {
            finalList.add( new ShortCost(-1, -1, 0f, "Не удалось разобрать"));
        }

        EditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);
        add_cost_comment.setText(getParam().getCostCreator().getComment());

       
        final RecyclerView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.list_add_costs);
        listViewCosts.setHasFixedSize(true);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.INSTANCE);
        listViewCosts.setLayoutManager(mLayoutManager);


        final RecyclerAdapterCostList adapter = new RecyclerAdapterCostList(MainActivity.INSTANCE, finalList);


        final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                ShortCost item = adapter.getItem(viewHolder.getAdapterPosition());
                List<ShortCost> costs = adapter.getData();

                List<ShortCost> costGroup = new ArrayList<>();
                int groupId = item.getGroupId();

                double sumGroup = 0;

                if ( item.isChange()) {
                    sumGroup = item.sum();
                }

                for (ShortCost c : costs) {
                    if ( c.isChange())
                        continue;

                    if (c.getGroupId() == groupId) {
                        if (!c.equals(item))
                            costGroup.add(c);

                        sumGroup += c.sum();
                    }
                }

                adapter.remove(viewHolder.getAdapterPosition());

                for (ShortCost c : costGroup) {
                    c.sum = sumGroup / costGroup.size();
                }


                listViewCosts.setAdapter(null);
                listViewCosts.setAdapter(adapter);


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

    @Override
    protected void helps() {

        MainActivity.INSTANCE.findViewById(R.id.helpHintDeleteMember).setVisibility(View.VISIBLE);
        MainActivity.INSTANCE.findViewById(R.id.helpHintAddCost).setVisibility(View.VISIBLE);
        MainActivity.INSTANCE.findViewById(R.id.helpHintCancel).setVisibility(View.VISIBLE);
        MainActivity.INSTANCE.findViewById(R.id.helpHintRefresh).setVisibility(View.VISIBLE);



    }





}
