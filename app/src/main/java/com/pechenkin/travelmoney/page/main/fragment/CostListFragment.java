package com.pechenkin.travelmoney.page.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
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

import java.io.File;
import java.util.List;


public class CostListFragment extends Fragment {

    private View fragmentView;
    private long scrollPosition = 0;


    private TripBaseTableRow selectTrip;

    public CostListFragment(TripBaseTableRow trip){
        this.selectTrip = trip;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_operation_list, container, false);
        MainActivity.INSTANCE.setTitle(this.selectTrip.name);

        FloatingActionButton mainPageSpeechRecognition = fragmentView.findViewById(R.id.mainPageSpeechRecognition);
        mainPageSpeechRecognition.setOnClickListener(view -> SpeechRecognitionHelper.run(MainActivity.INSTANCE));


        final ListView listViewCosts = fragmentView.findViewById(R.id.main_list);
        listViewCosts.setOnItemClickListener((parent, view, index, id) -> {

            ListAdapter adapter = listViewCosts.getAdapter();
            Cost item = (Cost) adapter.getItem(index);

            if (item != null && item.image_dir() != null && item.image_dir().length() > 0) {

                String realPath = item.image_dir();

                if (item.image_dir().contains(".provider")) {
                    //костыль, т.к. раньше в БД хранилось значение уже после работы FileProvider
                    String badPath = "content://" + MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider/external_files";
                    realPath = item.image_dir().replaceFirst(badPath, Environment.getExternalStorageDirectory().getAbsolutePath());
                }


                File file = new File(realPath);
                if (!file.exists()) {
                    Help.alertError("Файл не найден. " + realPath);
                    return;
                }

                Uri uri = FileProvider.getUriForFile(
                        MainActivity.INSTANCE,
                        MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(uri, "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                MainActivity.INSTANCE.startActivity(intent);

            }


        });

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

        final View buttonListToTop = fragmentView.findViewById(R.id.mainPageListToTop);
        listViewCosts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                scrollPosition = firstVisibleItem;
                if (firstVisibleItem > 1) {
                    buttonListToTop.setVisibility(View.VISIBLE);
                } else {
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

        printCostList();
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FloatingActionButton addCostButton = fragmentView.findViewById(R.id.mainPageAddbutton);
        if (addCostButton != null) {
            addCostButton.setOnClickListener(v -> {
                // Открываем мастер Добавления траты
                PageOpener.INSTANCE.open(MasterWho.class);
            });
        }

        //обновление списка потянув вниз
        SwipeRefreshLayout main_list_layout_refresh = fragmentView.findViewById(R.id.main_list_layout_refresh);
        main_list_layout_refresh.setOnRefreshListener(() -> {
            printCostList();
            main_list_layout_refresh.setRefreshing(false);
        });

        Help.showFabWithAnimation(fragmentView.findViewById(R.id.mainPageAddbutton));
        Help.showFabWithAnimation(fragmentView.findViewById(R.id.mainPageSpeechRecognition));

        printCostList();
    }

    private void printCostList() {
        ListView listViewCosts = fragmentView.findViewById(R.id.main_list);
        CostListBackground costListBackground = new CostListBackground(listViewCosts, this.selectTrip, null);
        costListBackground.execute();
    }
}
