package com.pechenkin.travelmoney.export.formats.send.types;

import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.export.formats.ExportFormat;

public interface SendType {

    void send(Trip pageTrip, ExportFormat exportFormat);

}
