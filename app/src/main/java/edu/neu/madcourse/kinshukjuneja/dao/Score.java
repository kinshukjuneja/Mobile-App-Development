package edu.neu.madcourse.kinshukjuneja.dao;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Score {

    private String uid;
    private int totalScore;
    private int highestScore;
    private String highestScoredWord;
    private long timestamp;

    public Score() {}

    public Score(String uid, int totalScore, int highestScore, String highestScoredWord, long timestamp) {
        this.uid = uid;
        this.totalScore = totalScore;
        this.highestScore = highestScore;
        this.highestScoredWord = highestScoredWord;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public String getHighestScoredWord() {
        return highestScoredWord;
    }

    public long getTimestamp() {
        return timestamp;
    }

}