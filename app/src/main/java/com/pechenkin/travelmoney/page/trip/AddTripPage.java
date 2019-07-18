package com.pechenkin.travelmoney.page.trip;

import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPageNew;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница добавления новой поездки
 */

public class AddTripPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPageNew.class, new PageParam.BuildingPageParam().setId(R.id.navigation_trips).getParam());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void addEvents() {
        FloatingActionButton commitButton = MainActivity.INSTANCE.findViewById(R.id.trip_add_button);
        commitButton.setOnClickListener(v -> {
            EditText trName = MainActivity.INSTANCE.findViewById(R.id.trip_name);
            EditText trComment = MainActivity.INSTANCE.findViewById(R.id.trip_comment);
            String strName = trName.getText().toString();
            if (strName.length() > 0) {
                if (t_trips.getIdByName(strName) == -1) {

                    long t_id = t_trips.add(strName, trComment.getText().toString());
                    t_trips.set_active(t_id);
                    PageOpener.INSTANCE.open(MainPageNew.class, new PageParam.BuildingPageParam().setId(R.id.navigation_members).getParam());
                } else {
                    Help.message("Поездка с таким названием уже существует");
                    Help.setActiveEditText(R.id.trip_name);
                }
            } else {
                Help.message("Введите название");
                Help.setActiveEditText(R.id.trip_name);
            }
        });
    }

    @Override
    protected int getPageId() {
        return R.layout.add_trip;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.addNewTrip);
    }

    @Override
    protected boolean fillFields() {
        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return R.id.trip_name;
    }


}
