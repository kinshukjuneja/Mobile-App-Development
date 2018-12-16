package edu.neu.madcourse.kinshukjuneja.utils.horoscope;

import edu.neu.madcourse.kinshukjuneja.R;

public enum HoroscopeType {

    PREDICTION("prediction", "Prediction", 0, 0),
    HEALTH("health", "Health", R.drawable.ic_health_selected, R.drawable.ic_health_unselected),
    PERSONAL_LIFE("personal_life", "Life", R.drawable.ic_life_selected, R.drawable.ic_life_unselected),
    PROFESSION("profession", "Work", R.drawable.ic_work_selected, R.drawable.ic_work_unselected),
    EMOTIONS("emotions", "Emotions", R.drawable.ic_emotions_selected, R.drawable.ic_emotions_unselected),
    TRAVEL("travel", "Travel", R.drawable.ic_travel_selected, R.drawable.ic_travel_unselected),
    LUCK("luck", "Luck", R.drawable.ic_luck_selected, R.drawable.ic_luck_unselected);

    private String jsonKey;
    private String verbose;
    private int selectedImg;
    private int unselectedImg;

    HoroscopeType(String jsonKey, String verbose, int selectedImg, int unselectedImg) {
        this.jsonKey = jsonKey;
        this.verbose = verbose;
        this.selectedImg = selectedImg;
        this.unselectedImg = unselectedImg;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public String getVerbose() {
        return verbose;
    }

    public int getSelectedImg() {
        return selectedImg;
    }

    public int getUnselectedImg() {
        return unselectedImg;
    }

}
