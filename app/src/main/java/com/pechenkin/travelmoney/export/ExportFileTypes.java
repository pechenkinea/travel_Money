package com.pechenkin.travelmoney.export;

import com.pechenkin.travelmoney.bd.table.row.TripBaseTableRow;
import com.pechenkin.travelmoney.export.formats.CSV;
import com.pechenkin.travelmoney.export.formats.ExportFormat;
import com.pechenkin.travelmoney.export.formats.JSON;
import com.pechenkin.travelmoney.export.formats.TotalDebt;
import com.pechenkin.travelmoney.export.formats.send.types.SendType;
import com.pechenkin.travelmoney.export.formats.send.types.SimpleText;
import com.pechenkin.travelmoney.export.formats.send.types.TextFile;

/**
 * Created by pechenkin on 02.04.2018.
 * Перечень типов для экспорта
 */

public enum ExportFileTypes {
    TOTAL_DEBT("Только итоги", new TotalDebt(), new SimpleText()),
    JSON("Все опреции в файл .json", new JSON(), new TextFile()),
    CSV("Все опреции в файл .csv", new CSV(), new TextFile());

    private final String fileType;
    private final ExportFormat exportFormat;
    private final SendType sendType;

    ExportFileTypes(String fType, ExportFormat exportFormat, SendType sendType) {
        this.fileType = fType;
        this.exportFormat = exportFormat;
        this.sendType = sendType;
    }

    public void send(TripBaseTableRow pageTrip){
        sendType.send(pageTrip, exportFormat);
    }

    public String toString() {
        return this.fileType;
    }

}
