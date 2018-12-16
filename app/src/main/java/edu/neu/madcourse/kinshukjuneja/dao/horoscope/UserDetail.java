package edu.neu.madcourse.kinshukjuneja.dao.horoscope;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDetail {

    private String uid;
    private String name;
    private String dateOfBirth;
    private String city;

    public UserDetail() {}

    public UserDetail(String uid, String name, String dateOfBirth, String city) {
        this.uid = uid;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCity() {
        return city;
    }

}