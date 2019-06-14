package com.example.sqlbrite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IdentificationDatabaseHelper extends SQLiteOpenHelper {

    private static final String DBNAME = "brite.db";
    public static final int CURRENTVERSION = 21;
    private static final String image_table = "image";
    private static final String text_table = "text";
    private static final String translation_table = "translation";
    private static final String front_id_card_table = "front_id_card";
    private static final String back_id_card_table = "back_id_card";
    private static final String business_license_table = "business_license";
    private static final String driver_license_table = "driver_license";
    private static final String driving_license_table = "driving_license";
    private static final String bank_card_table = "bank_card";
    private static final String license_plate_table = "license_plate";
    private static final String passport_table = "passport";
    private static final String train_ticket_table = "train_ticket";
    private static final String user = "user";
    private static IdentificationDatabaseHelper mHelper = null;
    private SQLiteDatabase mDB = null;

    private static final String CREATE_USER = "create table if not exists " + user + " ("
            + "id integer primary key autoincrement not null, "
            + "nickname text not null, "
            + "name text not null, "
            + "password text not null, "
            + "pic blob"
            + ");";

    private static final String CREATE_IMAGE = "create table if not exists " + image_table + " ("
            + "id integer primary key autoincrement not null, "
            + "score real not null, "
            + "root text not null, "
            + "keyword text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_TEXT = "create table if not exists " + text_table + " ("
            + "id integer primary key autoincrement not null, "
            + "words text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_TRANSLATION = "create table if not exists " + translation_table + " ("
            + "id integer primary key autoincrement not null, "
            + "original text not null, "
            + "translation text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_FRONT_IDCARD = "create table if not exists " + front_id_card_table + " ("
            + "id integer primary key autoincrement not null, "
            + "address text not null, "
            + "birthday text not null, "
            + "name text not null, "
            + "number text not null, "
            + "gender text not null, "
            + "nationality text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_BUSINESS_LICENSE = "create table if not exists " + business_license_table + " ("
            + "id integer primary key autoincrement not null, "
            + "registeredCapital text not null, "
            + "socialCreditCode text not null, "
            + "companyName text not null, "
            + "legalPerson text not null, "
            + "documentNumber text not null, "
            + "formation text not null, "
            + "dateOfEstablishment text not null, "
            + "companyAddress text not null, "
            + "businessScope text not null, "
            + "typeOfCompany text not null, "
            + "ValidityPeriod text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_BACK_IDCARD = "create table if not exists " + back_id_card_table + " ("
            + "id integer primary key autoincrement not null, "
            + "dateOfIssue text not null, "
            + "issuingAuthority text not null, "
            + "expirationDate text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_LICENSE_PLATE = "create table if not exists " + license_plate_table + " ("
            + "id integer primary key autoincrement not null, "
            + "color text not null, "
            + "number text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_BANK_CARD = "create table if not exists " + bank_card_table + " ("
            + "id integer primary key autoincrement not null, "
            + "bankCardNumber text not null, "
            + "validDate text not null, "
            + "bankName text not null, "
            + "bankCardType integer not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_DRIVER_LICENSE = "create table if not exists " + driver_license_table + " ("
            + "id integer primary key autoincrement not null, "
            + "address text not null, "
            + "birthday text not null, "
            + "name text not null, "
            + "certificateNumber text not null, "
            + "gender text not null, "
            + "countryOfCitizenship text not null, "
            + "quasiDrivingModel text not null, "
            + "initialLicenseDate text not null, "
            + "validityPeriod text not null, "
            + "deadline text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_DRIVING_LICENSE = "create table if not exists " + driving_license_table + " ("
            + "id integer primary key autoincrement not null, "
            + "address text not null, "
            + "numberPlateNumber text not null, "
            + "vehicleType text not null, "
            + "owner text not null, "
            + "natureOfUse text not null, "
            + "brandModelNumber text not null, "
            + "engineNumber text not null, "
            + "vehicleIdentificationNumber text not null, "
            + "registrationDate text not null, "
            + "issuingCertificateOfDate text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_PASSPORT = "create table if not exists " + passport_table + " ("
            + "id integer primary key autoincrement not null, "
            + "countryCode text not null, "
            + "passportIssuanceLocation text not null, "
            + "validUntil text not null, "
            + "passportNumber text not null, "
            + "passportDateOfIssue text not null, "
            + "birthPlace text not null, "
            + "name text not null, "
            + "pinyinOfName text not null, "
            + "birthday text not null, "
            + "sex text not null, "
            + "pic blob not null"
            + ");";

    private static final String CREATE_TRAIN_TICKET = "create table if not exists " + train_ticket_table + " ("
            + "id integer primary key autoincrement not null, "
            + "startingStation text not null, "
            + "destinationStation text not null, "
            + "seatCategory text not null, "
            + "ticketRates text not null, "
            + "ticketNum text not null, "
            + "trainNum text not null, "
            + "name text not null, "
            + "date text not null, "
            + "pic blob not null"
            + ");";

    private IdentificationDatabaseHelper(Context context) {
        super(context, DBNAME, null, CURRENTVERSION);
    }

    private IdentificationDatabaseHelper(Context context, int version) {
        super(context, DBNAME, null, version);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static IdentificationDatabaseHelper getInstance(Context context, int version) {
        if (version > 0 && mHelper == null) {
            mHelper = new IdentificationDatabaseHelper(context, version);
        } else if (mHelper == null) {
            mHelper = new IdentificationDatabaseHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_IMAGE);
        db.execSQL(CREATE_TEXT);
        db.execSQL(CREATE_TRANSLATION);
        db.execSQL(CREATE_FRONT_IDCARD);
        db.execSQL(CREATE_BACK_IDCARD);
        db.execSQL(CREATE_BUSINESS_LICENSE);
        db.execSQL(CREATE_LICENSE_PLATE);
        db.execSQL(CREATE_BANK_CARD);
        db.execSQL(CREATE_DRIVER_LICENSE);
        db.execSQL(CREATE_DRIVING_LICENSE);
        db.execSQL(CREATE_PASSPORT);
        db.execSQL(CREATE_TRAIN_TICKET);
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*switch (oldVersion) {
            case 20:
                *//*db.execSQL("drop table if exists image");
                db.execSQL("drop table if exists text");
                db.execSQL("drop table if exists train_ticket");
                db.execSQL("drop table if exists translation");
                db.execSQL("drop table if exists front_id_card");
                db.execSQL("drop table if exists back_id_card");
                db.execSQL("drop table if exists business_license");

                db.execSQL("drop table if exists driver_license");
                db.execSQL("drop table if exists driving_license");
                db.execSQL("drop table if exists bank_card");
                db.execSQL("drop table if exists license_plate");
                db.execSQL("drop table if exists passport");
                onCreate(db);*//*
                //db.execSQL(CREATE_FRONT_IDCARD);
                //db.execSQL(CREATE_BACK_IDCARD);
                db.execSQL("drop table if exists user");
                db.execSQL(CREATE_USER);
            default:
        }*/
    }
}
