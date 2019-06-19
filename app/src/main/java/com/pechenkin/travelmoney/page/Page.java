package com.pechenkin.travelmoney.page;

import android.view.MenuItem;

/**
 * Created by pechenkin on 19.04.2018.
 */

public interface Page {
    void open();
    void clickBackButton();
    boolean onOptionsItemSelected(MenuItem item);
    void addEvents();
    void setParam(PageParam param);
}
