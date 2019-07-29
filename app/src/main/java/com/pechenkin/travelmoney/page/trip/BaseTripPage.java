package com.pechenkin.travelmoney.page.trip;

import android.view.MenuItem;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 20.04.2018.
 * Стрвница редактирования поездки
 */

public abstract class BaseTripPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_trips).getParam());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected int getPageId() {
        return R.layout.edit_trip;
    }

    @Override
    protected int getFocusFieldId() {
        return R.id.trip_name;
    }


}
