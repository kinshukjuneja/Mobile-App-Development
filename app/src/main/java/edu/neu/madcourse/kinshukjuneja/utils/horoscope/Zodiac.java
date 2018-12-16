package edu.neu.madcourse.kinshukjuneja.utils.horoscope;

import edu.neu.madcourse.kinshukjuneja.R;

public enum Zodiac {

    ARIES(0, R.drawable.ic_aries, R.drawable.ic_aries_big),
    TAURUS(1, R.drawable.ic_taurus, R.drawable.ic_taurus_big),
    GEMINI(2, R.drawable.ic_gemini, R.drawable.ic_gemini_big),
    CANCER(3, R.drawable.ic_cancer, R.drawable.ic_cancer_big),
    LEO(4, R.drawable.ic_leo, R.drawable.ic_leo_big),
    VIRGO(5, R.drawable.ic_virgo, R.drawable.ic_virgo_big),
    LIBRA(6, R.drawable.ic_libra, R.drawable.ic_libra_big),
    SCORPIO(7, R.drawable.ic_scorpio, R.drawable.ic_scorpio_big),
    SAGITTARIUS(8, R.drawable.ic_sagittarius, R.drawable.ic_sagittarius_big),
    CAPRICORN(9, R.drawable.ic_capricorn, R.drawable.ic_capricorn_big),
    AQUARIUS(10, R.drawable.ic_aquarius, R.drawable.ic_aquarius_big),
    PISCES(11, R.drawable.ic_pisces, R.drawable.ic_pisces_big);

    public int id;
    public int image;
    public int bigImage;

    Zodiac(int id, int image, int bigImage) {
        this.id = id;
        this.image = image;
        this.bigImage = bigImage;
    }

    public static Zodiac getById(int id) {
        for(Zodiac zodiac : Zodiac.values()) {
            if(zodiac.id == id) return zodiac;
        }
        return null;
    }

}
