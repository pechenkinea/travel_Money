package com.pechenkin.travelmoney.page;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pechenkin.travelmoney.BuildConfig;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.SafeURLSpan;
import com.pechenkin.travelmoney.page.main.MainPageNew;

/**
 * Created by pechenkin on 11.05.2018.
 * О программе
 */

public class FaqPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPageNew.class, new PageParam.BuildingPageParam().setId(R.id.navigation_more).getParam());
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
        return R.layout.faq;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.help);
    }

    @Override
    protected boolean fillFields() {

        TextView whyCalc_q = MainActivity.INSTANCE.findViewById(R.id.whyCalc_q);
        whyCalc_q.setOnClickListener(view -> {
            TextView whyCalc_a = MainActivity.INSTANCE.findViewById(R.id.whyCalc_a);
            if (whyCalc_a.getVisibility() == View.GONE){
                whyCalc_a.setVisibility(View.VISIBLE);
            }
            else {
                whyCalc_a.setVisibility(View.GONE);
            }
        });

        TextView whyVoteAdd_q = MainActivity.INSTANCE.findViewById(R.id.whyVoteAdd_q);
        whyVoteAdd_q.setOnClickListener(view -> {
            TextView whyVoteAdd_a = MainActivity.INSTANCE.findViewById(R.id.whyVoteAdd_a);
            if (whyVoteAdd_a.getVisibility() == View.GONE){
                whyVoteAdd_a.setVisibility(View.VISIBLE);
            }
            else {
                whyVoteAdd_a.setVisibility(View.GONE);
            }
        });

        TextView faq_color_q = MainActivity.INSTANCE.findViewById(R.id.faq_color_q);
        faq_color_q.setOnClickListener(view -> {
            TextView faq_color_a = MainActivity.INSTANCE.findViewById(R.id.faq_color_a);
            if (faq_color_a.getVisibility() == View.GONE){
                faq_color_a.setVisibility(View.VISIBLE);
            }
            else {
                faq_color_a.setVisibility(View.GONE);
            }
        });

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }


}
