package com.pechenkin.travelmoney.export.formats.send.types;

import androidx.core.app.ShareCompat;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.export.formats.ExportFormat;

public class SimpleText implements SendType {

    @Override
    public void send(Trip pageTrip, ExportFormat exportFormat) {
        String text = exportFormat.getText(pageTrip);

        ShareCompat.IntentBuilder.from(MainActivity.INSTANCE)
                .setType(exportFormat.getMimeType())
                .setText(text)
                .startChooser();

    }

    public void send(String text) {

        ShareCompat.IntentBuilder.from(MainActivity.INSTANCE)
                .setType("text/plain")
                .setText(text)
                .startChooser();

    }


}
