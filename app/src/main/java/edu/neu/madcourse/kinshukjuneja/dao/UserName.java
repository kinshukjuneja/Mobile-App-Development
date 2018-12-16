package edu.neu.madcourse.kinshukjuneja.dao;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserName {

    private String uid;
    private String username;

    public UserName() {}

    public UserName(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }
}