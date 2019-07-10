package com.pechenkin.travelmoney.page.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.cost.Cost;
import com.pechenkin.travelmoney.cost.GroupCost;
import com.pechenkin.travelmoney.export.Export;
import com.pechenkin.travelmoney.export.ExportFileTypes;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.cost.add.master.MasterWho;
import com.pechenkin.travelmoney.page.trip.TripsListPage;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by pechenkin on 19.04.2018.
 * Главная страница со списком итогов и перечнем всех операций
 */

public class MainPage extends BasePage {

    private static long scrollPosition = 0;
    private static long changeCostStateTime = 0;
    private static long refreshClickTime = 0;


    @Override
    public void clickBackButton() {
        if (hasParam() && getParam().getId() > -1) {
            PageOpener.INSTANCE.open(TripsListPage.class);
        } else
            MainActivity.INSTANCE.finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.exportToJson:
                Export.export(item, ExportFileTypes.JSON, pageTrip);
                return true;

            case R.id.exportToCSV:
                Export.export(item, ExportFileTypes.CSV, pageTrip);
                return true;

            case R.id.refreshCostList:
                printCostList();
                return true;


            default:
                return false;
        }
    }

    private boolean revertStatus(Cost item) {
        if (item != null && item.id() >= 0) {
            if (item.active() == 1) {
                t_costs.disable_cost(item.id());
                item.setActive(0);
            } else {
                item.setActive(1);
                t_costs.enable_cost(item.id());
            }
            //обновляем список
            ListView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.main_list);
            listViewCosts.invalidateViews();
            changeCostStateTime = new Date().getTime();
            refreshClickTime = 0;
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
                ListView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.main_list);
                listViewCosts.invalidateViews();
                changeCostStateTime = new Date().getTime();
                refreshClickTime = 0;
                return true;

            }
        }

        return false;
    }

    @Override
    public void addEvents() {

        Button addCostButton = MainActivity.INSTANCE.findViewById(R.id.mainPageAddbutton);
        if (addCostButton != null) {
            addCostButton.setOnClickListener(v -> {
                // Открываем мастер Добавления траты
                PageOpener.INSTANCE.open(MasterWho.class);
            });
        }

        ImageButton mainPageSpeechRecognition = MainActivity.INSTANCE.findViewById(R.id.mainPageSpeechRecognition);

        mainPageSpeechRecognition.setOnClickListener(view -> SpeechRecognitionHelper.run(MainActivity.INSTANCE));


        Button mainPageRevertbutton = MainActivity.INSTANCE.findViewById(R.id.mainPageRevertbutton);
        if (mainPageRevertbutton != null) {
            mainPageRevertbutton.setOnClickListener(v -> clickBackButton());
        }


        final ListView listViewCosts = MainActivity.INSTANCE.findViewById(R.id.main_list);
        listViewCosts.setOnItemClickListener((parent, view, index, id) -> {


            ListAdapter adapter = listViewCosts.getAdapter();
            Cost item = (Cost) adapter.getItem(index);

            if (new Date().getTime() - changeCostStateTime < 2000) {
                revertStatus(item);
            } else {
                if (item != null && item.image_dir() != null && item.image_dir().length() > 0) {

                    String realPath = item.image_dir();

                    if (item.image_dir().contains(".provider")){
                        //костыль, т.к. раньше в БД хранилось значение уже после работы FileProvider
                        String badPath = "content://" + MainActivity.INSTANCE.getApplicationContext().getPackageName() + ".provider/external_files";
                        realPath = item.image_dir().replaceFirst(badPath, Environment.getExternalStorageDirectory().getAbsolutePath());
                    }


                    File file = new File(realPath);
                    if (!file.exists()){
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
            }

        });


        listViewCosts.setOnItemLongClickListener((arg0, v, index, arg3) -> {

            ListAdapter adapter = listViewCosts.getAdapter();
            Cost item = (Cost) adapter.getItem(index);
            return revertStatus(item);
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
        return "";
    }

    private TripBaseTableRow getPageTrip() {
        return pageTrip;
    }

    private TripBaseTableRow pageTrip = t_trips.ActiveTrip;

    @Override
    protected boolean fillFields() {

        refreshClickTime = 0;
        changeCostStateTime = 0;
        String readerCaption = "";
        if (hasParam() && getParam().getId() > -1) {

            //Если режим просмотра поездки, не помеченной "по умолчанию"
            pageTrip = t_trips.getTripById(getParam().getId());
            if (getPageTrip() == null)
                return false;

            MainActivity.INSTANCE.findViewById(R.id.mainPageAddbutton).setVisibility(View.INVISIBLE);
            MainActivity.INSTANCE.findViewById(R.id.mainPageSpeechRecognition).setVisibility(View.INVISIBLE);

            MainActivity.INSTANCE.findViewById(R.id.mainPageRevertbutton).setVisibility(View.VISIBLE);

            readerCaption = " (" + MainActivity.INSTANCE.getString(R.string.readMode) + ")";
        }

        Toolbar toolbar = MainActivity.INSTANCE.findViewById(R.id.toolbar_main);
        if (toolbar != null) {
            MainActivity.INSTANCE.setSupportActionBar(toolbar);
            if (getPageTrip() != null && getPageTrip().name != null && MainActivity.INSTANCE.getSupportActionBar() != null)
                MainActivity.INSTANCE.getSupportActionBar().setTitle(getPageTrip().name + readerCaption);
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
        changeCostStateTime = 0;
        //Что бы слишком часто не жали на кнопку обновления
        if (new Date().getTime() - refreshClickTime < 5000) {
            return;
        }
        refreshClickTime = new Date().getTime();

        CostListBackground costListBackground = new CostListBackground(getPageTrip());
        costListBackground.execute();

    }
}
