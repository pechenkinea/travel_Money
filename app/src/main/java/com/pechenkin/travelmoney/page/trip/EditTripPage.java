package com.pechenkin.travelmoney.page.trip;

import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.local.table.query.row.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;

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
            if (strName.length() > 0) {
                if (t_trips.isAdded(strName)) {
                    if (getParam().getId() != t_trips.getIdByName(strName)) {
                        Help.message("Название занято");
                        Help.setActiveEditText(R.id.trip_name);
                        return;
                    }
                }

                t_trips.edit(getParam().getId(), strName, getTextInputEditText(trComment));
                CheckBox isActive = MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction);

                if (isActive.isChecked())
                    t_trips.set_active(getParam().getId());

                Help.message("Сохранено");
                PageOpener.INSTANCE.open(MainPage.class, new PageParam.BuildingPageParam().setId(R.id.navigation_members).getParam());
            } else {
                Help.message("Введите название");
                Help.setActiveEditText(R.id.trip_name);
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

        TripTableRow trip = t_trips.getTripById(getParam().getId());
        if (trip == null) {
            Help.message("Ошибка. Не найдена поездка с id " + getParam().getId());
            return false;
        }

        TextInputEditText t_name = MainActivity.INSTANCE.findViewById(R.id.trip_name);
        t_name.setText(trip.name);

        TextInputEditText t_comment = MainActivity.INSTANCE.findViewById(R.id.trip_comment);
        t_comment.setText(trip.comment);

        MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction).setVisibility(View.VISIBLE);

        CheckBox isActive = MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction);
        if (t_trips.isActive(trip.id))
            isActive.setChecked(true);
        else
            isActive.setChecked(false);


        return true;
    }


}
