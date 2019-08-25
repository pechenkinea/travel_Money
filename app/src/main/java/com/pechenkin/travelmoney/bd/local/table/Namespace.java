package com.pechenkin.travelmoney.bd.local.table;

/**
 * Created by pechenkin on 09.04.2018.
 * Поля БД
 */

public interface Namespace {

    String DB_NAME = "travelMoney.db";

    String TABLE_TRIPS = "trips";
    String TABLE_TRIPS_MEMBERS = "tripsmembers";
    String TABLE_COSTS = "costs";
    String TABLE_TRANSACTION = "transactions";
    String TABLE_TRANSACTION_ITEMS = "transaction_items";
    String TABLE_MEMBERS = "members";

    String TABLE_SETTINGS = "settings";
    String TABLE_COLORS = "colors";

    String FIELD_ID = "_id";
    String FIELD_NAME = "name";
    String FIELD_COLOR = "color";
    String FIELD_ICON = "icon";
    String FIELD_COMMENT = "comment";
    String FIELD_PROCESSED = "processed";
    String FIELD_TRIP = "trip";
    String FIELD_MEMBER = "member";
    String FIELD_TO_MEMBER = "to_member";
    String FIELD_SUM = "sum";

    String FIELD_CREDIT = "credit";
    String FIELD_DEBIT = "debit";
    String FIELD_TRANSACTION = "transactionId";

    String FIELD_IMAGE_DIR = "image_dir";
    String FIELD_DATE = "date";
    String FIELD_ACTIVE = "active";
    String FIELD_VALUE = "value";
    String FIELD_REPAYMENT = "repayment";


}
