package edu.neu.madcourse.kinshukjuneja.utils.horoscope;

public class ZodiacHelper {

    private static int[][] compatibilityTable = {
            {60, 65, 65, 65, 90, 45, 70, 80, 90, 50, 55, 65},
            {60, 70, 70, 80, 70, 90, 75, 85, 50, 95, 80, 85},
            {70, 70, 75, 60, 80, 75, 90, 60, 75, 50, 90, 50},
            {65, 80, 60, 75, 70, 75, 60, 95, 55, 45, 70, 90},
            {90, 70, 80, 70, 85, 75, 65, 75, 95, 45, 70, 75},
            {45, 90, 75, 75, 75, 70, 80, 85, 70, 95, 50, 70},
            {70, 75, 90, 60, 65, 80, 80, 85, 80, 85, 95, 50},
            {80, 85, 60, 95, 75, 85, 85, 90, 80, 65, 60, 95},
            {90, 50, 75, 55, 95, 70, 80, 85, 85, 55, 60, 75},
            {50, 95, 50, 45, 45, 95, 85, 65, 55, 85, 70, 85},
            {55, 80, 90, 70, 70, 50, 95, 60, 60, 70, 80, 55},
            {65, 85, 50, 90, 75, 70, 50, 95, 75, 85, 55, 80}
    };

    /**
     * ref : https://cultureastrology.com/zodiac-signs/
     * @param dateOfBirth
     * @return
     */
    public static Zodiac getZodiacFromDob(String dateOfBirth) {
        String[] dobArr = dateOfBirth.split("-");
        int month = Integer.parseInt(dobArr[1]);
        int date = Integer.parseInt(dobArr[2]);

        if((month == 3 && date >= 21) || (month == 4 && date <= 19)) return Zodiac.ARIES;
        else if((month == 4 && date >= 20) || (month == 5 && date <= 20)) return Zodiac.TAURUS;
        else if((month == 5 && date >= 21) || (month == 6 && date <= 20)) return Zodiac.GEMINI;
        else if((month == 6 && date >= 21) || (month == 7 && date <= 22)) return Zodiac.CANCER;
        else if((month == 7 && date >= 23) || (month == 8 && date <= 22)) return Zodiac.LEO;
        else if((month == 8 && date >= 23) || (month == 9 && date <= 22)) return Zodiac.VIRGO;
        else if((month == 9 && date >= 23) || (month == 10 && date <= 22)) return Zodiac.LIBRA;
        else if((month == 10 && date >= 23) || (month == 11 && date <= 21)) return Zodiac.SCORPIO;
        else if((month == 11 && date >= 22) || (month == 12 && date <= 21)) return Zodiac.SAGITTARIUS;
        else if((month == 12 && date >= 22) || (month == 1 && date <= 19)) return Zodiac.CAPRICORN;
        else if((month == 1 && date >= 20) || (month == 2 && date <= 18)) return Zodiac.AQUARIUS;
        else return Zodiac.PISCES;
    }

    public static void updateCompatibiltyWithFriends() {
        for(Friend friend : HoroscopeCache.friends) {
            friend.setCompatibility(getCompatibilityWith(friend.getZodiac()));
        }
    }

    public static int getCompatibilityWith(Zodiac zodiac) {
        return compatibilityTable[HoroscopeCache.zodiac.id][zodiac.id];
    }

}
