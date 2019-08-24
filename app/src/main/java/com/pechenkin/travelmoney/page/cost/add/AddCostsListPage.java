package com.pechenkin.travelmoney.page.cost.add;

import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;

/**
 * Created by pechenkin on 19.04.2018.
 * Страница для добаваления трат при голосовом вводе
 */
public class AddCostsListPage extends BasePage {


    @Override
    protected int getPageId() {
        return R.layout.add_cost_list;
    }

    @Override
    protected String getTitleHeader() {
        return "Добавить траты" + "(" + TripManager.INSTANCE.getActiveTrip().getName() + ")";
    }

    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class);
    }


    @Override
    public void addEvents() {



    }


    private void refreshForm() {
        EditText add_costs_text = MainActivity.INSTANCE.findViewById(R.id.add_costs_text);
        String text = add_costs_text.getText().toString();
        TextInputEditText add_cost_comment = MainActivity.INSTANCE.findViewById(R.id.add_cost_comment);

        if (text.length() > 0) {
            CostCreator cc = new CostCreator(text, getTextInputEditText(add_cost_comment));
            PageParam param = new PageParam().setDraftTransaction(cc.getDraftTransaction());
            PageOpener.INSTANCE.open(AddCostsListPage.class, param);
        }

        MainActivity.INSTANCE.findViewById(R.id.add_costs_list_refresh_button).setVisibility(View.INVISIBLE);
    }

    @Override
    protected boolean fillFields() {


            return false;



    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }


}
