package com.example.contactsbuddy.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contactsbuddy.AdapterController.ContactListView_Adapter;
import com.example.contactsbuddy.Helpers.DBConstants;
import com.example.contactsbuddy.Helpers.DBHelperClass;
import com.example.contactsbuddy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ShowContactsList_Activity extends AppCompatActivity {

    private EditText contact_search_bar;
    private FloatingActionButton contact_add_btn, contact_sort_btn;
    private LinearLayout search_contact_message;
    private RecyclerView contacts_list_view;
    private TextView contacts_count;

    private DBHelperClass dbHelper;
    private ContactListView_Adapter contactListViewAdapter;
    private String sort_new_contacts = DBConstants.ADDED_ON + " DESC";
    private String sort_old_contacts = DBConstants.ADDED_ON + " ASC";
    private String sort_name_inAsc = DBConstants.CONTACT_NAME + " ASC";
    private String sort_name_inDsc = DBConstants.CONTACT_NAME + " DESC";
    private String sortFromSelection;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts_list);

        contact_search_bar = (EditText) findViewById(R.id.contacts_search_bar);
        contact_add_btn = (FloatingActionButton) findViewById(R.id.contact_add_float_btn);
        contact_sort_btn = (FloatingActionButton) findViewById(R.id.contact_sort_float_btn);
        search_contact_message = (LinearLayout) findViewById(R.id.search_results_message);
        contacts_list_view = (RecyclerView) findViewById(R.id.show_contact_list_view);
        contacts_count = (TextView) findViewById(R.id.contact_count_txt);
        dbHelper = new DBHelperClass(this);

        sortFromSelection = sort_name_inAsc;
        renderContactsOnList(sortFromSelection);

        contact_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowContactsList_Activity.this, AddContact_Activity.class);
                startActivity(intent);
            }
        });

        contact_search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (charSequence != null && charSequence.length() > 0) {
                        contactListViewAdapter.getFilter().filter(charSequence);
                        if (contactListViewAdapter.getItemCount() == 0) {
                            contacts_list_view.setVisibility(View.GONE);
                            search_contact_message.setVisibility(View.VISIBLE);
                        } else {
                            search_contact_message.setVisibility(View.GONE);
                            contacts_list_view.setVisibility(View.VISIBLE);
                        }
                        contacts_count.setText("Contacts: " +  contactListViewAdapter.getItemCount() + ".");
                    } else {
                        renderContactsOnList(sortFromSelection);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contact_sort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortOptionsList();
            }
        });

    }

    private void renderContactsOnList(String sort_from) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contacts_list_view.setLayoutManager(linearLayoutManager);
        contactListViewAdapter = new ContactListView_Adapter(ShowContactsList_Activity.this, dbHelper.listAllContacts(sort_from));
        contacts_list_view.setAdapter(contactListViewAdapter);
        contacts_count.setText("Contacts: " + dbHelper.totalContactsCount() + ".");
    }

    private void sortOptionsList() {
        String[] options = {"Name (From: A-Z)", "Name (From: Z-A)", "New Contacts", "Old Contacts"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowContactsList_Activity.this);
        builder.setTitle("Select to sort by:")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i == 0) {
                            renderContactsOnList(sort_name_inAsc);

                        } else if (i == 1) {
                            renderContactsOnList(sort_name_inDsc);

                        } else if (i == 2) {
                            renderContactsOnList(sort_new_contacts);

                        } else if (i == 3) {
                            renderContactsOnList(sort_old_contacts);
                        }
                    }
                }).show();
    }

    protected void onResume() {
        super.onResume();
        renderContactsOnList(sortFromSelection);
    }
}