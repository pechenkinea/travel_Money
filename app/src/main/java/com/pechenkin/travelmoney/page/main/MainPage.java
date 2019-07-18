package com.pechenkin.travelmoney.page.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.trip.TripsListPage;

import java.io.File;

/**
 * Created by pechenkin on 19.04.2018.
 * Раньше была главной страницей для отображения списка операций.
 * Сейчас используется только для просмотра списка операций по старой поездке
 */

public class MainPage extends BasePage {

    private static long scrollPosition = 0;

    @Override
    public void clickBackButton() {
        if (hasParam() && getParam().getId() > -1) {
            PageOpener.INSTANCE.open(TripsListPage.class);
        } else
            PageOpener.INSTANCE.open(MainPageNew.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    @Override
    public void addEvents() {


        FloatingActionButton mainPageRevertButton = MainActivity.INSTANCE.findViewById(R.id.mainPageRevertButton);
        if (mainPageRevertButton != null) {
            mainPageRevertButton.setOnClickListener(v -> clickBackButton());
        }


        final ListView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.main_list);
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


        final View buttonListToTop = MainActivity.INSTANCE.findViewById(R.id.mainPageListToTop);
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

    }

    @Override
    protected int getPageId() {
        return R.layout.main_start_page;
    }

    @Override
    protected String getTitleHeader() {
        return getPageTrip().name;
    }

    private TripBaseTableRow getPageTrip() {
        return pageTrip;
    }

    private TripBaseTableRow pageTrip = t_trips.ActiveTrip;

    @Override
    protected boolean fillFields() {

        if (hasParam() && getParam().getId() > -1) {

            //Если режим просмотра поездки, не помеченной "по умолчанию"
            pageTrip = t_trips.getTripById(getParam().getId());
            if (getPageTrip() == null)
                return false;

            MainActivity.INSTANCE.findViewById(R.id.mainPageAddbutton).setVisibility(View.INVISIBLE);
            MainActivity.INSTANCE.findViewById(R.id.mainPageSpeechRecognition).setVisibility(View.INVISIBLE);

            MainActivity.INSTANCE.findViewById(R.id.mainPageRevertButton).setVisibility(View.VISIBLE);


            MainActivity.INSTANCE.setTitle(pageTrip.name + " (" + MainActivity.INSTANCE.getString(R.string.readMode) + ")");
        }


        printCostList();

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }

    @Override
    protected void helps() {

        if (hasParam() && getParam().getId() > -1) {
            return;
        }

        if (t_settings.INSTANCE.active(NamespaceSettings.MAIN_PAGE_HELP_ADD_COST_BUTTON)) {
            MainActivity.INSTANCE.findViewById(R.id.helpHintAddCost).setVisibility(View.VISIBLE);
            MainActivity.INSTANCE.findViewById(R.id.helpHintAddCostVoice).setVisibility(View.VISIBLE);
        }

        if (t_settings.INSTANCE.active(NamespaceSettings.MAIN_PAGE_HELP_ADD_MEMBERS)) {
            MembersQueryResult membersInCurrentTrip = t_members.getAllByTripId(getPageTrip().id);
            if (!membersInCurrentTrip.hasRows() || membersInCurrentTrip.getAllRows().length < 2) {
                TextView helpHintAddMembers = MainActivity.INSTANCE.findViewById(R.id.helpHintAddMembers);
                helpHintAddMembers.setVisibility(View.VISIBLE);
            }
        }
    }

    private void printCostList() {
        ListView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.main_list);
        CostListBackground costListBackground = new CostListBackground(listViewCosts, getPageTrip(), null);
        costListBackground.execute();

    }
}
