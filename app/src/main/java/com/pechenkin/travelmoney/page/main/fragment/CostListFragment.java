package com.pechenkin.travelmoney.page.main.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.GroupCost;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.cost.add.master.MasterWho;
import com.pechenkin.travelmoney.page.main.CostListBackground;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;

import java.util.List;


public class CostListFragment extends BaseMainPageFragment {

    private long scrollPosition = 0;

    private TripBaseTableRow selectTrip;
    private boolean readMode = false;

    public CostListFragment(TripBaseTableRow trip) {
        this.selectTrip = trip;
        readMode = true;
    }

    public CostListFragment() {
        this.selectTrip = t_trips.ActiveTrip;
    }

    @Override
    int getViewId() {
        return R.layout.fragment_operation_list;
    }

    @Override
    void setListeners() {

        final ListView listViewCosts = fragmentView.findViewById(R.id.main_list);

        if (readMode) {
            fragmentView.findViewById(R.id.mainPageAddbutton).setVisibility(View.GONE);
            fragmentView.findViewById(R.id.mainPageSpeechRecognition).setVisibility(View.GONE);
        } else {

            FloatingActionButton mainPageSpeechRecognition = fragmentView.findViewById(R.id.mainPageSpeechRecognition);
            mainPageSpeechRecognition.setOnClickListener(view -> SpeechRecognitionHelper.run(MainActivity.INSTANCE));

            listViewCosts.setOnItemLongClickListener((arg0, v, index, arg3) -> {

                ListAdapter adapter = listViewCosts.getAdapter();
                Cost item = (Cost) adapter.getItem(index);

                if (item != null && item.id() >= 0) {
                    if (item.active() == 1) {
                        t_costs.disable_cost(item.id());
                        item.setActive(0);
                    } else {
                        item.setActive(1);
                        t_costs.enable_cost(item.id());
                    }
                    //обновляем список
                    listViewCosts.invalidateViews();
                    return true;
                }
                if (item instanceof GroupCost) {

                    List<Cost> groupCosts = ((GroupCost) item).getCosts();
                    if (groupCosts.size() > 0) {
                        long statusFirst = groupCosts.get(0).active();
                        if (statusFirst == 1) {
                            for (Cost cost : groupCosts) {
                                t_costs.disable_cost(cost.id());
                                cost.setActive(0);
                            }
                        } else {
                            for (Cost cost : groupCosts) {
                                cost.setActive(1);
                                t_costs.enable_cost(cost.id());
                            }
                        }
                        ((GroupCost) item).updateSum();
                        //обновляем список
                        listViewCosts.invalidateViews();
                        return true;
                    }
                }

                return false;
            });

            FloatingActionButton addCostButton = fragmentView.findViewById(R.id.mainPageAddbutton);
            if (addCostButton != null) {
                addCostButton.setOnClickListener(v -> {
                    // Открываем мастер Добавления траты
                    PageOpener.INSTANCE.open(MasterWho.class);
                });
            }
        }


        final FloatingActionButton buttonListToTop = fragmentView.findViewById(R.id.mainPageListToTop);
        listViewCosts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                scrollPosition = firstVisibleItem;
                if (firstVisibleItem > 1) {
                    if (buttonListToTop.getVisibility() == View.INVISIBLE) {
                        Help.showFabWithAnimation(buttonListToTop);
                    }
                } else {
                    buttonListToTop.setAnimation(null);
                    buttonListToTop.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonListToTop.setOnClickListener(v -> {
            if (scrollPosition > 10) {
                listViewCosts.setSelection(10);
            }
            listViewCosts.smoothScrollToPosition(0);
        });


        //обновление списка потянув вниз
        SwipeRefreshLayout main_list_layout_refresh = fragmentView.findViewById(R.id.main_list_layout_refresh);
        main_list_layout_refresh.setOnRefreshListener(() -> {
            printCostList();
            main_list_layout_refresh.setRefreshing(false);
        });
    }

    @Override
    public void doAfterRender() {
        printCostList();
    }

    @Override
    int[] getButtons() {
        if (!readMode) {
            return new int[]{
                    R.id.mainPageAddbutton,
                    R.id.mainPageSpeechRecognition
            };
        } else {
            return new int[0];
        }
    }

    private void printCostList() {
        ListView listViewCosts = fragmentView.findViewById(R.id.main_list);
        CostListBackground costListBackground = new CostListBackground(listViewCosts, this.selectTrip);
        costListBackground.execute();
    }
}
