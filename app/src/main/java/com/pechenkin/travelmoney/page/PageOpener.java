package com.pechenkin.travelmoney.page;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 19.04.2018.
 */

public class PageOpener {
    private PageOpener(){

    }
    public static final PageOpener INSTANCE;
    static {
        INSTANCE = new PageOpener();
    }

    private Page currentPage = null;

    public Page getCurrentPage(){
        if (currentPage == null)
        {
            open(MainPage.class);
        }
        return  currentPage;
    }

    public  void open(Class<? extends Page> pageClass)
    {
        open(pageClass, null);
    }

    public  void open(Class<? extends Page> pageClass, PageParam param)
    {
        MainActivity.INSTANCE.setRefreshActon(null);

        Page page;
        try {
            page = pageClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            Help.alert(e.getMessage());
            return;
        }

        page.setParam(param);
        currentPage = page;
        //TimeMeter openPageTimer = new TimeMeter("Открытие страницы " + pageClass.getName());
        page.open();
        //openPageTimer.stop();
    }



}
