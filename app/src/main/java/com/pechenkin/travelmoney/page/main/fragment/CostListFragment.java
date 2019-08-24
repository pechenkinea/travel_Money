package com.pechenkin.travelmoney.page.main.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.SettingsTable;
import com.pechenkin.travelmoney.transaction.adapter.AdapterCostList;
import com.pechenkin.travelmoney.transaction.adapter.CostListItem;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.cost.add.master.MasterWho;
import com.pechenkin.travelmoney.page.main.CostListBackground;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;


public class CostListFragment extends BaseMainPageFragment {

    private long scrollPosition = 0;

    private final Trip selectTrip;
    private boolean readOnly = false;

    public CostListFragment(Trip trip) {
        this.selectTrip = trip;
        this.readOnly = true;
    }

    public CostListFragment() {
        this.selectTrip = TripManager.INSTANCE.getActiveTrip();
    }

    @Override
    int getViewId() {
        return R.layout.fragment_operation_list;
    }

    @Override
    void setListeners() {

        final ListView listViewCosts = fragmentView.findViewById(R.id.main_list);

        if (this.readOnly) {
            fragmentView.findViewById(R.id.mainPageAddButton).setVisibility(View.GONE);
            fragmentView.findViewById(R.id.mainPageSpeechRecognition).setVisibility(View.GONE);
        } else {

            FloatingActionButton mainPageSpeechRecognition = fragmentView.findViewById(R.id.mainPageSpeechRecognition);
            mainPageSpeechRecognition.setOnClickListener(view -> SpeechRecognitionHelper.run(MainActivity.INSTANCE));

            FloatingActionButton addCostButton = fragmentView.findViewById(R.id.mainPageAddButton);
            addCostButton.setOnClickListener(v -> PageOpener.INSTANCE.open(MasterWho.class));


            listViewCosts.setOnItemLongClickListener((arg0, v, index, arg3) -> {

                AdapterCostList adapter = (AdapterCostList)listViewCosts.getAdapter();
                CostListItem item = adapter.getItem(index);
                if (item.onLongClick()) {
                    listViewCosts.invalidateViews();
                    return true;
                }

                return false;

            });


        }


        final FloatingActionButton buttonListToTop = fragmentView.findViewById(R.id.mainPageListToTop);
        listViewCosts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                scrollPosition = firstVisibleItem;
                if (firstVisibleItem > 1) {
                    if (buttonListToTop.getVisibility() == View.INVISIBLE) {
                        Help.showFabWithAnimation(buttonListToTop);
                    }
                } else {
                    buttonListToTop.setAnimation(null);
                    ((View) buttonListToTop).setVisibility(View.INVISIBLE);
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

        MainActivity.INSTANCE.setRefreshActon(this::printCostList);

        printCostList();
    }

    @Override
    int[] getButtons() {
        if (!this.readOnly) {
            return new int[]{
                    R.id.mainPageAddButton,
                    R.id.mainPageSpeechRecognition
            };
        } else {
            return new int[0];
        }
    }

    private void printCostList() {

        CostListBackground costListBackground = new CostListBackground(this.readOnly ,this.selectTrip, (finalList) -> {

            ListView listViewCosts = fragmentView.findViewById(R.id.main_list);
            AdapterCostList adapter = new AdapterCostList(MainActivity.INSTANCE.getApplicationContext(), finalList);
            listViewCosts.setAdapter(adapter);

            if (adapter.getCount() > 5 && !SettingsTable.INSTANCE.active(NamespaceSettings.DELETE_COST_SHOWED_HELP)) {
                Help.alertHelp(MainActivity.INSTANCE.getString(R.string.deleteCostHelp));

                SettingsTable.INSTANCE.setActive(NamespaceSettings.DELETE_COST_SHOWED_HELP, true);
            }

        });
        costListBackground.execute();

    }
}
