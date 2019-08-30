package com.pechenkin.travelmoney.page.trip;

import android.view.View;
import android.widget.CheckBox;

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
 * Стрвница редактирования поездки
 */

public class EditTripPage extends BaseTripPage {


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
                return;
            }



            Trip trip = getParam().getTrip();

            if (trip != null) {
                trip.edit(strName, getTextInputEditText(trComment));
                CheckBox isActive = MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction);

                if (isActive.isChecked())
                    TripManager.INSTANCE.setActive(getParam().getTrip());

                Help.message("Сохранено");

                PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_members));
            } else {
                Help.message("Ошибка");
            }


        });
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.editTrip);
    }

    @Override
    protected boolean fillFields() {
        if (!hasParam()) {
            Help.message("Ошибка. Нет поездки для редактирования");
            return false;
        }

        Trip trip = getParam().getTrip();
        if (trip == null) {
            Help.message("Ошибка. Не задана поездка");
            return false;
        }

        TextInputEditText t_name = MainActivity.INSTANCE.findViewById(R.id.trip_name);
        t_name.setText(trip.getName());

        TextInputEditText t_comment = MainActivity.INSTANCE.findViewById(R.id.trip_comment);
        t_comment.setText(trip.getComment());

        MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction).setVisibility(View.VISIBLE);

        CheckBox isActive = MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction);
        if (TripManager.INSTANCE.isActive(trip))
            isActive.setChecked(true);
        else
            isActive.setChecked(false);


        return true;
    }


}
