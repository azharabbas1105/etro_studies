package com.ingentive.leaderboard.model;

/**
 * Created by PC on 04-10-2016.
 */
public class TrialModel {
    int id;
    int rank;
    long timeStamp;
    String testNme;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTestNme() {
        return testNme;
    }

    public void setTestNme(String testNme) {
        this.testNme = testNme;
    }

    public TrialModel(){

    }
}
