package com.pechenkin.travelmoney.export.formats;

import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;

public interface ExportFormat {

    String getText(TripBaseTableRow pageTrip);

    String getMimeType();
    String getExpansionType();
}
