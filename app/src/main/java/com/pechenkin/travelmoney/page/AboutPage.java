package com.pechenkin.travelmoney.page;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.pechenkin.travelmoney.BuildConfig;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.SafeURLSpan;

/**
 * Created by pechenkin on 11.05.2018.
 * О программе
 */

public class AboutPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MainPage.class);
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
        return R.layout.about;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.about);
    }

    @Override
    protected boolean fillFields() {
        TextView versionView = (TextView) MainActivity.INSTANCE.findViewById(R.id.about_version);
        versionView.setText(String.format("%s %s", versionView.getText(), BuildConfig.VERSION_NAME));

        TextView versionCodeView = (TextView) MainActivity.INSTANCE.findViewById(R.id.about_version_code);
        versionCodeView.setText(String.format("%s %s", versionCodeView.getText(), BuildConfig.VERSION_CODE));

        TextView developerName = (TextView) MainActivity.INSTANCE.findViewById(R.id.developerName);
        developerName.setText( SafeURLSpan.parseSafeHtml("Разработчик: <a href=\"https://vk.com/id2871931\">Печёнкин Евгений</a>"));
        developerName.setMovementMethod(LinkMovementMethod.getInstance());

        TextView developerEmail = (TextView) MainActivity.INSTANCE.findViewById(R.id.developerEmail);
        developerEmail.setText(SafeURLSpan.parseSafeHtml("Email: <a href=\"pechenkin.e.a@gmail.com\">pechenkin.e.a@gmail.com</a>"));
        developerEmail.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkToPlayMarket = (TextView) MainActivity.INSTANCE.findViewById(R.id.linkToPlayMarket);
        linkToPlayMarket.setText( SafeURLSpan.parseSafeHtml("<a href=\"https://play.google.com/store/apps/details?id=com.pechenkin.travelmoney\">TravelMoney в PlayMarket</a>"));
        linkToPlayMarket.setMovementMethod(LinkMovementMethod.getInstance());




        ((TextView)MainActivity.INSTANCE.findViewById(R.id.thank_aps))
                .setText(Html.fromHtml("<b>Арбузову Петру</b>. За идеи по функционалу, тестирование и описание для PlayMarket'a"));

        ((TextView)MainActivity.INSTANCE.findViewById(R.id.thank_pma))
                .setText(Html.fromHtml("<b>Печёнкиной Марине</b>. За идеи по функционалу и тестирование"));

        ((TextView)MainActivity.INSTANCE.findViewById(R.id.thank_psv))
                .setText(Html.fromHtml("<b>Подлесных Сергею</b>. За идеи по функционалу"));


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
