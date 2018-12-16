package edu.neu.madcourse.kinshukjuneja.utils.horoscope;

import java.util.Objects;

import edu.neu.madcourse.kinshukjuneja.utils.horoscope.Zodiac;

public class Friend {

    private String name;
    private String city;
    private String dob;
    private int compatibility;
    private Zodiac zodiac;
    private String key;

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setCompatibility(int compatibility) {
        this.compatibility = compatibility;
    }

    public void setZodiac(Zodiac zodiac) {
        this.zodiac = zodiac;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Friend(String name, String city, String dob, int compatibility, Zodiac zodiac, String key) {
        this.name = name;
        this.city = city;
        this.dob = dob;
        this.compatibility = compatibility;
        this.zodiac = zodiac;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDob() {
        return dob;
    }

    public int getCompatibility() {
        return compatibility;
    }

    public Zodiac getZodiac() {
        return zodiac;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;
        Friend friend = (Friend) o;
        return Objects.equals(key, friend.key);
    }

}
