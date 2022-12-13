package com.example.contactsbuddy.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsbuddy.Helpers.DBConstants;
import com.example.contactsbuddy.Helpers.DBHelperClass;
import com.example.contactsbuddy.R;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewContactDetails_Activity extends AppCompatActivity {

    private CircleImageView contact_profile_image;
    private ImageView edit_icon, delete_icon, call_icon, message_icon, email_icon, back_btn;
    private TextView view_contact_name, c_contact_name, c_contact_number, c_contact_email;
    private String id, contact_name, contact_number, contact_email, contact_image, contact_added_on, contact_updated_on;
    private DBHelperClass dbHelperClass;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        contact_profile_image = (CircleImageView) findViewById(R.id.contact_profile_img);
        edit_icon = (ImageView) findViewById(R.id.v_c_edit_btn);
        delete_icon = (ImageView) findViewById(R.id.v_c_delete_btn);
        call_icon = (ImageView) findViewById(R.id.phone_icon_btn);
        message_icon = (ImageView) findViewById(R.id.message_icon_btn);
        email_icon = (ImageView) findViewById(R.id.email_icon_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        view_contact_name = (TextView) findViewById(R.id.view_contact_name);
        c_contact_name = (TextView) findViewById(R.id.v_c_contact_name);
        c_contact_number = (TextView) findViewById(R.id.v_c_contact_number);
        c_contact_email = (TextView) findViewById(R.id.v_c_contact_email);

        id = getIntent().getStringExtra("ID");

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        dbHelperClass = new DBHelperClass(this);

        call_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(contact_number))));
            }
        });

        message_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + Uri.encode(contact_number))));
            }
        });

        email_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(contact_email))));
            }
        });

        edit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewContactDetails_Activity.this, EditContactDetails_Activity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedContact();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewSelectedContact();
    }

    private void viewSelectedContact() {
        String selectQuery = "SELECT * FROM " + DBConstants.TABLE_NAME +
                " WHERE " + DBConstants.ID + "=" + id;

        SQLiteDatabase contact_db = dbHelperClass.getReadableDatabase();
        Cursor cursor = contact_db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id = "" + cursor.getInt(cursor.getColumnIndexOrThrow(DBConstants.ID));
                contact_name = "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_NAME));
                contact_number = "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_NUMBER));
                contact_email = "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_EMAIL));
                contact_image = "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.CONTACT_IMAGE));
                contact_added_on = "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.ADDED_ON));
                contact_updated_on = "" + cursor.getString(cursor.getColumnIndexOrThrow(DBConstants.UPDATED_ON));

                view_contact_name.setText(contact_name);
                c_contact_name.setText(contact_name);
                c_contact_number.setText(contact_number);
                c_contact_email.setText(contact_email);

                if (contact_image.equals("") || contact_image.equals("null")) {
                    contact_profile_image.setImageResource(R.drawable.profile_avatar);
                } else {
                    contact_profile_image.setImageURI(Uri.parse(contact_image));
                }

            } while (cursor.moveToNext());
        }
    }

    private void deleteSelectedContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewContactDetails_Activity.this);
        builder.setTitle("Delete Contact:")
                .setMessage("Do you want to delete this contact permanently '" + contact_name + "' ?")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelperClass.deleteContact(id);
                        Toast.makeText(ViewContactDetails_Activity.this, "" + contact_name + " deleted successfully!", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    protected void onResume() {
        super.onResume();
        viewSelectedContact();
    }
}