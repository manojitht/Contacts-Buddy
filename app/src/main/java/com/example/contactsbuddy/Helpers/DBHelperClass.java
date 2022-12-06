package com.example.contactsbuddy.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.contactsbuddy.Models.ContactModel;

import java.util.ArrayList;

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

    public ArrayList<ContactModel> listAllContacts (String orderBy) {
        ArrayList<ContactModel> contactsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBConstants.TABLE_NAME + " ORDER BY " + orderBy;
        SQLiteDatabase contact_db = this.getWritableDatabase();
        Cursor cursor = contact_db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ContactModel contact = new ContactModel(
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_NAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_NUMBER)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_EMAIL)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.ADDED_ON)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.UPDATED_ON)));

                contactsList.add(contact);
            } while (cursor.moveToNext());
        }
        contact_db.close();
        return contactsList;
    }

    public void updateContact(String id, String contact_name, String contact_number, String contact_email, String contact_image, String updated_on) {
        SQLiteDatabase contact_db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBConstants.CONTACT_NAME, contact_name);
        values.put(DBConstants.CONTACT_NUMBER, contact_number);
        values.put(DBConstants.CONTACT_EMAIL, contact_email);
        values.put(DBConstants.CONTACT_IMAGE, contact_image);
        values.put(DBConstants.UPDATED_ON, updated_on);

        contact_db.update(DBConstants.TABLE_NAME, values, DBConstants.ID + "=?", new String[]{id});

        contact_db.close();
    }

    public void deleteContact(String id) {
        SQLiteDatabase contact_db = getWritableDatabase();
        contact_db.delete(DBConstants.TABLE_NAME, DBConstants.ID + "=?", new String[]{id});
        contact_db.close();
    }

    public int totalContactsCount() {
        String countQuery = "SELECT * FROM " + DBConstants.TABLE_NAME;
        SQLiteDatabase contact_db = this.getReadableDatabase();
        Cursor cursor = contact_db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
