package com.example.contactsbuddy.Models;

public class ContactModel {

    String id, contact_name, contact_number, contact_email, contact_image, added_on, updated_on;

    public ContactModel(String id, String contact_name, String contact_number, String contact_email, String contact_image, String added_on, String updated_on) {
        this.id = id;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.contact_email = contact_email;
        this.contact_image = contact_image;
        this.added_on = added_on;
        this.updated_on = updated_on;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }

    public String getAdded_on() {
        return added_on;
    }

    public void setAdded_on(String added_on) {
        this.added_on = added_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}
