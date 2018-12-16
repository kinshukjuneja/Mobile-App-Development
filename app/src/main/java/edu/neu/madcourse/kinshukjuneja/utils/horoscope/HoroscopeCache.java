package edu.neu.madcourse.kinshukjuneja.utils.horoscope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.kinshukjuneja.dao.horoscope.UserDetail;

public class HoroscopeCache {

    public static UserDetail currentUser;
    public static Zodiac zodiac;
    public static List<Friend> friends = new ArrayList<>();
    public static Map<Zodiac, List<Friend>> friendsByZodiacMap = new HashMap<>();
    public static List<Friend> nearMe = new ArrayList<>();
    public static String key;
    public static HoroscopeNotificationHelper notificationHelper;

    public static Map<Zodiac, String> dailyHealthHoroscope = new HashMap<>();
    public static Map<Zodiac, String> dailyPersonalLifeHoroscope = new HashMap<>();
    public static Map<Zodiac, String> dailyProfessionHoroscope = new HashMap<>();
    public static Map<Zodiac, String> dailyEmotionHoroscope = new HashMap<>();
    public static Map<Zodiac, String> dailyTravelHoroscope = new HashMap<>();
    public static Map<Zodiac, String> dailyLuckHoroscope = new HashMap<>();

    public static void clearCache() {
        friends = new ArrayList<>();
        friendsByZodiacMap = new HashMap<>();
        nearMe = new ArrayList<>();
    }

}
