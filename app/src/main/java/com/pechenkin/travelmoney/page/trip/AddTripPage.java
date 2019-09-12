package com.pechenkin.travelmoney.page.trip;

import android.view.View;
import android.widget.CheckBox;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.TripStore;
import com.pechenkin.travelmoney.bd.local.query.TripTableRow;
import com.pechenkin.travelmoney.bd.local.table.TableTrip;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.RunWithProgressBar;

/**
 * Created by pechenkin on 20.04.2018.
 * Страница добавления новой поездки
 */

public class AddTripPage extends BaseTripPage {


    @Override
    public void addEvents() {

        CheckBox isRemote = MainActivity.INSTANCE.findViewById(R.id.trip_remote_check);

        TextInputEditText remoteUuid = MainActivity.INSTANCE.findViewById(R.id.trip_remote_uuid);

        isRemote.setOnCheckedChangeListener((compoundButton, check) -> {

            if (check) {
                remoteUuid.setVisibility(View.VISIBLE);
            } else {
                remoteUuid.setText("");
                remoteUuid.setVisibility(View.GONE);
            }

        });

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

            String uuid = getTextInputEditText(remoteUuid).trim();

            if (uuid.length() > 0) {
                TripTableRow existTrip = TableTrip.INSTANCE.getByUuid(uuid);
                if (existTrip != null) {
                    Help.message("Поездка с таким id уже добавлена. (" + existTrip.name + ")");
                    Help.setActiveEditText(R.id.trip_remote_uuid);
                    return;
                }
            }

            TripStore tripStore;

            if (isRemote.isChecked()) {
                tripStore = TripStore.FIRESTORE;
            } else {
                tripStore = TripStore.LOCAL;
            }

            new RunWithProgressBar<>(
                    () -> {
                        Trip t = TripManager.INSTANCE.add(strName, getTextInputEditText(trComment), tripStore, uuid);
                        TripManager.INSTANCE.setActive(t);
                        return null;
                    },
                    o -> PageOpener.INSTANCE.open(MainPage.class, new PageParam().setFragmentId(R.id.navigation_members))
            );


        });
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.addNewTrip);
    }

    @Override
    protected boolean fillFields() {
        MainActivity.INSTANCE.findViewById(R.id.trip_remote_check).setEnabled(true);

        if (getParam().getUri() != null){
            CheckBox remoteCheck = MainActivity.INSTANCE.findViewById(R.id.trip_remote_check);
            remoteCheck.setChecked(true);

            TextInputEditText remoteUuid = MainActivity.INSTANCE.findViewById(R.id.trip_remote_uuid);
            remoteUuid.setVisibility(View.VISIBLE);
            remoteUuid.setText(getParam().getUri().getQueryParameter("id"));

        }
        return true;
    }


}
