package com.pechenkin.travelmoney.page.trip;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.member.MembersListPage;
import com.pechenkin.travelmoney.page.PageOpenner;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница добавления новой поездки
 */

public class AddTripPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(TripsListPage.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void addEvents() {
        Button commitButton = (Button) MainActivity.INSTANCE.findViewById(R.id.trip_add_button);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText trName = (EditText) MainActivity.INSTANCE.findViewById(R.id.trip_name);
                EditText trComment = (EditText) MainActivity.INSTANCE.findViewById(R.id.trip_comment);
                String strName = trName.getText().toString();
                if (strName.length() > 0)
                {
                    if (t_trips.getIdByName(strName) == -1) {

                        long t_id = t_trips.add(strName, trComment.getText().toString());
                        t_trips.set_active(t_id);
                        PageOpenner.INSTANCE.open(MembersListPage.class);
                    }
                    else
                    {
                        Help.message("Поездка с таким названием уже существует");
                        Help.setActiveEditText(R.id.trip_name);
                    }
                }
                else
                {
                    Help.message("Введите название");
                    Help.setActiveEditText(R.id.trip_name);
                }
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

    @Override
    protected void helps() {

    }
}
