package com.pechenkin.travelmoney.page.trip;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.utils.Help;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница добавления новой поездки
 */

public class AddTripPage extends BaseTripPage {


    @Override
    public void addEvents() {
        FloatingActionButton commitButton = MainActivity.INSTANCE.findViewById(R.id.trip_commit_button);
        commitButton.setOnClickListener(v -> {
            TextInputEditText trName = MainActivity.INSTANCE.findViewById(R.id.trip_name);
            TextInputEditText trComment = MainActivity.INSTANCE.findViewById(R.id.trip_comment);
            String strName = getTextInputEditText(trName);
            if (strName.length() == 0) {
                Help.message("Введите название");
                Help.setActiveEditText(R.id.trip_name);
            }

            Trip t = TripManager.INSTANCE.add(strName, getTextInputEditText(trComment));
            TripManager.INSTANCE .setActive(t);
            PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_members));
        });
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.addNewTrip);
    }

    @Override
    protected boolean fillFields() {
        return true;
    }


}
