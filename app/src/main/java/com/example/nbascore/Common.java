package com.example.nbascore;

public class Common {
    private String scoreData;
    private static Common instance = new Common();
    public static Common getInstance()
    {
        return instance;
    }

    public String getScoreData() {
        return scoreData;
    }

    public void setScoreData(String scoreData) {
        this.scoreData = scoreData;
    }
}
