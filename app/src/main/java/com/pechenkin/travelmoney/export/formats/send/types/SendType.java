package com.pechenkin.travelmoney.export.formats.send.types;

import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.export.formats.ExportFormat;

public interface SendType {

    void send(TripBaseTableRow pageTrip, ExportFormat exportFormat);

}
