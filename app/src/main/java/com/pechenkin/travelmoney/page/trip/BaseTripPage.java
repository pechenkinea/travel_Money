package com.pechenkin.travelmoney.page.trip;

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
        PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_trips));
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
