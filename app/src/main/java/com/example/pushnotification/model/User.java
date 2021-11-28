package com.example.pushnotification.model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String username;
    private String password;
    private String name;
    private Uri uriImage;

    public User(String name, Uri uriImage) {
        this.name = name;
        this.uriImage = uriImage;
    }

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Uri getUriImage() {
        return uriImage;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
