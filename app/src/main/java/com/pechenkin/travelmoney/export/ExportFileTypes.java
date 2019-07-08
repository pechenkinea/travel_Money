package com.pechenkin.travelmoney.export;

import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.export.formats.CSV;
import com.pechenkin.travelmoney.export.formats.ExportFormat;
import com.pechenkin.travelmoney.export.formats.JSON;

/**
 * Created by pechenkin on 02.04.2018.
 * Перечень типов для экспорта
 */

public enum ExportFileTypes {
    JSON("json", "application/json", new JSON()),
    CSV("csv", "text/csv", new CSV());

    private final String fileType;
    private final String mimeType ;
    private final ExportFormat exportFormat;

    ExportFileTypes(String fType, String mType, ExportFormat exportFormat) {
        this.fileType = fType;
        this.mimeType = mType;
        this.exportFormat = exportFormat;
    }

    public String getText(TripBaseTableRow pageTrip){
        return  exportFormat.getText(pageTrip);
    }

    public String toString() {
        return this.fileType;
    }

    public String getMimeTypeName() {
        return this.mimeType;
    }
}
