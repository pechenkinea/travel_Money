package com.pechenkin.travelmoney.export.formats.send.types;

import com.pechenkin.travelmoney.bd.table.query.trip.TripTableRow;
import com.pechenkin.travelmoney.export.formats.ExportFormat;

public interface SendType {

    void send(TripTableRow pageTrip, ExportFormat exportFormat);

}
