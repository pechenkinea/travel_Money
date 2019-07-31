package com.pechenkin.travelmoney.page;

import android.graphics.drawable.Drawable;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 11.05.2018.
 * О программе
 */

public class FaqPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_more).getParam());
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

        SparseIntArray textFields = new SparseIntArray();

        textFields.put(R.id.whyCalc_q, R.id.whyCalc_a);
        textFields.put(R.id.whyVoteAdd_q, R.id.whyVoteAdd_a);
        textFields.put(R.id.faq_color_q, R.id.faq_color_a);
        textFields.put(R.id.faq_other_phone_q, R.id.faq_other_phone_a);

        Drawable upIcon = AppCompatResources.getDrawable(MainActivity.INSTANCE, R.drawable.ic_up_24);
        Drawable downIcon = AppCompatResources.getDrawable(MainActivity.INSTANCE, R.drawable.ic_down_24);


        for (int i = 0; i < textFields.size(); i++) {
            int q = textFields.keyAt(i);
            int a = textFields.get(q);

            TextView questionField = MainActivity.INSTANCE.findViewById(q);
            questionField.setOnClickListener(view -> {
                TextView answerField = MainActivity.INSTANCE.findViewById(a);
                if (answerField.getVisibility() == View.GONE) {
                    answerField.setVisibility(View.VISIBLE);
                    questionField.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, upIcon, null);
                } else {
                    answerField.setVisibility(View.GONE);
                    questionField.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, downIcon, null);
                }
            });
        }

        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }


}
