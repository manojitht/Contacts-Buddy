package com.example.contactsbuddy.Helpers;

public class DBConstants {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "CONTACTS_BUDDY_DB";
    public static final String TABLE_NAME = "CONTACTS_TABLE";
    public static final String ID = "ID";
    public static final String CONTACT_NAME = "CONTACT_NAME";
    public static final String CONTACT_NUMBER = "CONTACT_NUMBER";
    public static final String CONTACT_EMAIL = "CONTACT_EMAIL";
    public static final String CONTACT_IMAGE = "CONTACT_IMAGE";
    public static final String ADDED_ON = "ADDED_ON";
    public static final String UPDATED_ON = "UPDATED_ON";

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CONTACT_NAME + " TEXT," +
            CONTACT_NUMBER + " TEXT," +
            CONTACT_EMAIL + " TEXT," +
            CONTACT_IMAGE + " TEXT," +
            ADDED_ON + " TEXT," +
            UPDATED_ON + " TEXT" + ")";
}
