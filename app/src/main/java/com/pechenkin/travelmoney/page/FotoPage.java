package com.pechenkin.travelmoney.page;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

/**
 * Created by pechenkin on 24.04.2018.
 * Страница для простотра фото по трате
 */

public class FotoPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MainPage.class, getParam());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void addEvents() {

    }

    @Override
    protected int getPageId() {
        return R.layout.image_foto_cost;
    }

    @Override
    protected String getTitleHeader() {
        return "";
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected boolean fillFields() {

        if (!hasParam() || getParam().getFotoUrl().length() == 0)
            return false;


        WebView webView = MainActivity.INSTANCE.findViewById(R.id.foto_cost_imageFullSctreen);

        webView.setBackgroundColor(android.R.color.black);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setPadding(0, 0, 0, 0);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Help.message("Фото не найдено");
                PageOpenner.INSTANCE.open(MainPage.class);
            }
        });


        webView.loadUrl(getParam().getFotoUrl());

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }

    @Override
    protected void helps() {

    }
}
