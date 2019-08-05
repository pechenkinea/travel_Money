package com.pechenkin.travelmoney.page;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.pechenkin.travelmoney.BuildConfig;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.SafeURLSpan;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.lang.invoke.ConstantCallSite;

/**
 * Created by pechenkin on 11.05.2018.
 * О программе
 */

public class AboutPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_more).getParam());
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

        TextView versionView = MainActivity.INSTANCE.findViewById(R.id.about_version);
        versionView.setText(String.format("%s %s", versionView.getText(), BuildConfig.VERSION_NAME));

        TextView versionCodeView = MainActivity.INSTANCE.findViewById(R.id.about_version_code);
        versionCodeView.setText(String.format("%s %s", versionCodeView.getText(), BuildConfig.VERSION_CODE));

        TextView developerName = MainActivity.INSTANCE.findViewById(R.id.developerName);
        developerName.setText(SafeURLSpan.parseSafeHtml("Разработчик: <a style='color=#005f98' href=\"https://vk.com/id2871931\">Печёнкин Евгений</a>"));
        developerName.setMovementMethod(LinkMovementMethod.getInstance());

        TextView developerEmail = MainActivity.INSTANCE.findViewById(R.id.developerEmail);
        developerEmail.setText(SafeURLSpan.parseSafeHtml("Email: <a href=\"pechenkin.e.a@gmail.com\">pechenkin.e.a@gmail.com</a>"));
        developerEmail.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkToPlayMarket = MainActivity.INSTANCE.findViewById(R.id.linkToPlayMarket);
        linkToPlayMarket.setText(SafeURLSpan.parseSafeHtml("<a href=\"" + TMConst.TM_APP_URL + "\">TravelMoney в PlayMarket</a>"));
        linkToPlayMarket.setMovementMethod(LinkMovementMethod.getInstance());

        TextView linkToGitHub = MainActivity.INSTANCE.findViewById(R.id.linkToGitHub);
        linkToGitHub.setText(SafeURLSpan.parseSafeHtml("<a href=\"https://github.com/pechenkinea/travel_Money\">Репозиторий TravelMoney на GitHub</a>"));
        linkToGitHub.setMovementMethod(LinkMovementMethod.getInstance());


        ((TextView) MainActivity.INSTANCE.findViewById(R.id.thank_aps))
                .setText(Html.fromHtml("<b>Арбузову Петру</b>. За идеи по функционалу, тестирование и описание для PlayMarket'a"));

        ((TextView) MainActivity.INSTANCE.findViewById(R.id.thank_pma))
                .setText(Html.fromHtml("<b>Печёнкиной Марине</b>. За идеи по функционалу и тестирование"));

        ((TextView) MainActivity.INSTANCE.findViewById(R.id.thank_psv))
                .setText(Html.fromHtml("<b>Подлесных Сергею</b>. За идеи по функционалу"));


        return true;
    }


}
