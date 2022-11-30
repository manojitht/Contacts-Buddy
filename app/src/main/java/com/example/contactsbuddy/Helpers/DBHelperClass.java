package com.example.contactsbuddy.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperClass extends SQLiteOpenHelper {

    public DBHelperClass(@Nullable Context context) {
        super(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConstants.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long createContact(String contact_name, String contact_number, String contact_email, String contact_image, String added_on, String updated_on) {
        SQLiteDatabase contact_db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBConstants.CONTACT_NAME, contact_name);
        values.put(DBConstants.CONTACT_NUMBER, contact_number);
        values.put(DBConstants.CONTACT_EMAIL, contact_email);
        values.put(DBConstants.CONTACT_IMAGE, contact_image);
        values.put(DBConstants.ADDED_ON, added_on);
        values.put(DBConstants.UPDATED_ON, updated_on);

        long result = contact_db.insert(DBConstants.TABLE_NAME, null, values);

        contact_db.close();

        return result;
    }
}
