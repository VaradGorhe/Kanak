package com.example.eyickanak.model;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : Leaderboard.java
        Global Variables:None
        Functions   :
        */


//class Leaderboard is created which has name, contact and email as variables.

public class User {
    String name, contact, email;

    public User() {
    }

    public User(String name, String contact, String email) {
        this.name = name;
        this.contact = contact;
        this.email = email;
    }


    //The following functions return the values of name,contact and email of the particular user.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
