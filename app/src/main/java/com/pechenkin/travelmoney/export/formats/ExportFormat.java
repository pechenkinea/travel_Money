package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.bd.Trip;

public interface ExportFormat {

    String getText(Trip pageTrip);

    String getMimeType();
    String getExpansionType();
}
