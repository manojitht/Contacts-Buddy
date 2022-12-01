package com.example.contactsbuddy.AdapterController;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsbuddy.Helpers.SearchContacts;
import com.example.contactsbuddy.Models.ContactModel;
import com.example.contactsbuddy.R;
import com.example.contactsbuddy.Views.ViewContactDetails_Activity;

import java.util.ArrayList;

public class ContactListView_Adapter extends RecyclerView.Adapter<ContactListView_Adapter.HolderContact> implements Filterable {

    private Context context;
    public ArrayList<ContactModel> contactList, filterList;
    private SearchContacts searchContactsFilter;

    public ContactListView_Adapter(Context context, ArrayList<ContactModel> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.filterList = contactList;
    }

    @NonNull
    @Override
    public ContactListView_Adapter.HolderContact onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_card_layout, parent, false);

        return new HolderContact(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListView_Adapter.HolderContact holder, int position) {

        ContactModel contact = contactList.get(position);
        String contact_id = contact.getId();
        String contact_name = contact.getContact_name();
        String contact_number = contact.getContact_number();
        String contact_image = contact.getContact_image();

        if (contact_image.equals("") || contact_image.equals("null")) {
            holder.contact_profile_image.setImageResource(R.drawable.profile_avatar);
        } else {
            holder.contact_profile_image.setImageURI(Uri.parse(contact_image));
        }

        holder.contact_profile_name.setText(contact_name);
        holder.contact_profile_number.setText("Phone: "+ contact_number);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewContactDetails_Activity.class);
                intent.putExtra("ID", contact_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        if (searchContactsFilter == null) {
            searchContactsFilter = new SearchContacts(this, filterList);
        }
        return searchContactsFilter;
    }

    class HolderContact extends RecyclerView.ViewHolder {

        ImageView contact_profile_image;
        TextView contact_profile_name, contact_profile_number;

        public HolderContact (@Nullable View itemView) {
            super(itemView);

            contact_profile_image = itemView.findViewById(R.id.contact_profile_image);
            contact_profile_name = itemView.findViewById(R.id.contact_profile_name);
            contact_profile_number = itemView.findViewById(R.id.contact_profile_number);
        }
    }
}
