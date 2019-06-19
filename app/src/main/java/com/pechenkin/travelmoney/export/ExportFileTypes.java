package com.pechenkin.travelmoney.export;

/**
 * Created by pechenkin on 02.04.2018.
 * Перечень типов для экспорта
 */

public enum ExportFileTypes {
    JSON("json", "application/json"),
    CSV("csv", "text/csv");

    private final String fileType;
    private final String mimeType ;

    ExportFileTypes(String fType, String mType) {
        fileType = fType;
        mimeType = mType;
    }

    public String toString() {
        return this.fileType;
    }

    public String getMimeTypeName() {
        return this.mimeType;
    }
}
