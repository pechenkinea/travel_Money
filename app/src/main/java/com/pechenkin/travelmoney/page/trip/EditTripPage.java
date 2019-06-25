package com.pechenkin.travelmoney.page.trip;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpenner;
import com.pechenkin.travelmoney.page.member.MembersListPage;

/**
 * Created by pechenkin on 20.04.2018.
 * Стрвница редактирования поездки
 */

public class EditTripPage extends BasePage {
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
        Button commitButton = MainActivity.INSTANCE.findViewById(R.id.edit_trip_commit_button);
        commitButton.setOnClickListener(v -> {

            EditText trName =  MainActivity.INSTANCE.findViewById(R.id.edit_trip_ET_Name);
            EditText trComment =  MainActivity.INSTANCE.findViewById(R.id.edit_trip_ET_Comment);

            String strName = trName.getText().toString();
            if (strName.length() > 0)
            {
                if (t_trips.isAdded(strName))
                {
                    if (getParam().getId() != t_trips.getIdByName(strName))
                    {
                        Help.message("Название занято");
                        Help.setActiveEditText(R.id.edit_trip_ET_Name);
                        return;
                    }
                }

                t_trips.edit(getParam().getId(), strName, trComment.getText().toString());
                CheckBox isActive = MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction);

                if (isActive.isChecked())
                    t_trips.set_active(getParam().getId());

                Help.message("Сохранено");
                PageOpenner.INSTANCE.open(MembersListPage.class);
            }
            else
            {
                Help.message("Введите название");
                Help.setActiveEditText(R.id.edit_trip_ET_Name);
            }

        });
    }

    @Override
    protected int getPageId() {
        return R.layout.edit_trip;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.editTrip);
    }

    @Override
    protected boolean fillFields() {
        if (!hasParam())
        {
            Help.message("Ошибка. Нет поездки для редактирования");
            return false;
        }

        TripBaseTableRow trip = t_trips.getTripById(getParam().getId());
        if (trip == null)
        {
            Help.message("Ошибка. Не найдена поездка с id " + getParam().getId());
            return false;
        }

        EditText t_name =  MainActivity.INSTANCE.findViewById(R.id.edit_trip_ET_Name);
        t_name.setText(trip.name);

        EditText t_comment =  MainActivity.INSTANCE.findViewById(R.id.edit_trip_ET_Comment);
        t_comment.setText(trip.comment);

        CheckBox isActive = MainActivity.INSTANCE.findViewById(R.id.edit_trip_checkAction);
        if (t_trips.isActive(trip.id))
            isActive.setChecked(true);
        else
            isActive.setChecked(false);


        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return  R.id.edit_trip_ET_Name;
    }

    @Override
    protected void helps() {

    }
}
