package com.pechenkin.travelmoney.page;

import com.pechenkin.travelmoney.Help;

/**
 * Created by pechenkin on 19.04.2018.
 */

public class PageOpenner {
    private PageOpenner(){

    }
    public static PageOpenner INSTANCE;
    static {
        INSTANCE = new PageOpenner();
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
        //TimeMeter openPageTimemetr = new TimeMeter("Открытие страницы " + pageClass.getName());
        page.open();
        //openPageTimemetr.stop();
    }



}
