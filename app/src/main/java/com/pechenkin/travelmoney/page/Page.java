package com.pechenkin.travelmoney.page;

/**
 * Created by pechenkin on 19.04.2018.
 */

public interface Page {
    void open();
    void clickBackButton();
    void addEvents();
    void setParam(PageParam param);
}
