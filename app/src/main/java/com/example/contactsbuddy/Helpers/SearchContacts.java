package com.example.contactsbuddy.Helpers;


import android.widget.Filter;

import com.example.contactsbuddy.AdapterController.ContactListView_Adapter;
import com.example.contactsbuddy.Models.ContactModel;

import java.util.ArrayList;

public class SearchContacts extends Filter {
    private ContactListView_Adapter contactListViewAdapter;
    private ArrayList<ContactModel> contactSearchList;

    public SearchContacts(ContactListView_Adapter contactListViewAdapter, ArrayList<ContactModel> contactSearchList) {
        this.contactListViewAdapter = contactListViewAdapter;
        this.contactSearchList = contactSearchList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults =new FilterResults();

        if (charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<ContactModel> contactsList = new ArrayList<>();

            for (int i = 0; i < contactSearchList.size(); i++) {
                if (contactSearchList.get(i).getContact_name().toUpperCase().contains(charSequence)) {
                    contactsList.add(contactSearchList.get(i));
                }
            }
            filterResults.count = contactsList.size();
            filterResults.values = contactsList;
        } else {
            filterResults.count = contactSearchList.size();
            filterResults.values = contactSearchList;
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        contactListViewAdapter.contactList = (ArrayList<ContactModel>) filterResults.values;
        contactListViewAdapter.notifyDataSetChanged();
    }
}
