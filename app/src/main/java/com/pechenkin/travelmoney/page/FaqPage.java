package com.pechenkin.travelmoney.page;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.HashMap;
import java.util.Map;

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

        @SuppressLint("UseSparseArrays")
        HashMap<Integer, Integer> textFields = new HashMap<>();

        textFields.put(R.id.whyCalc_q, R.id.whyCalc_a);
        textFields.put(R.id.whyVoteAdd_q, R.id.whyVoteAdd_a);
        textFields.put(R.id.faq_color_q, R.id.faq_color_a);
        textFields.put(R.id.faq_other_phone_q, R.id.faq_other_phone_a);

        for(Map.Entry<Integer, Integer> entry : textFields.entrySet()) {
            Integer q = entry.getKey();
            Integer a = entry.getValue();

            TextView questionField = MainActivity.INSTANCE.findViewById(q);
            questionField.setOnClickListener(view -> {
                TextView answerField = MainActivity.INSTANCE.findViewById(a);
                if (answerField.getVisibility() == View.GONE){
                    answerField.setVisibility(View.VISIBLE);
                    questionField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_24, 0);
                }
                else {
                    answerField.setVisibility(View.GONE);
                    questionField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_24, 0);
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