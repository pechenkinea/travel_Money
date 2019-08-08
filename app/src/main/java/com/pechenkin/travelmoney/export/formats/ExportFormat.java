package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.bd.table.query.trip.TripTableRow;

public interface ExportFormat {

    String getText(TripTableRow pageTrip);

    String getMimeType();
    String getExpansionType();
}
