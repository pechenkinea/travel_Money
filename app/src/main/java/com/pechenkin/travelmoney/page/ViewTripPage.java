package com.pechenkin.travelmoney.page;

import androidx.fragment.app.FragmentManager;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.row.TripTableRow;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.page.main.fragment.CostListFragment;

/**
 * Created by pechenkin on 19.04.2018.
 * Используется только для просмотра списка операций по старой поездке
 */
public class ViewTripPage extends BasePage {

    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_trips).getParam());
    }




    @Override
    public void addEvents() {

    }

    @Override
    protected int getPageId() {
        return R.layout.main_start_page;
    }

    @Override
    protected String getTitleHeader() {
        return "";
    }

    private TripTableRow getPageTrip() {
        return pageTrip;
    }

    private TripTableRow pageTrip = t_trips.getActiveTrip();

    @Override
    protected boolean fillFields() {

        if (hasParam() && getParam().getId() > -1) {
            //Если режим просмотра поездки, не помеченной "по умолчанию"
            pageTrip = t_trips.getTripById(getParam().getId());
        }

        if (pageTrip == null)
            return false;

        MainActivity.INSTANCE.setTitle(getPageTrip().name + " (" + MainActivity.INSTANCE.getString(R.string.readMode) + ")");

        FragmentManager manager = MainActivity.INSTANCE.getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment, new CostListFragment(pageTrip)).commit();

        return true;
    }

}
