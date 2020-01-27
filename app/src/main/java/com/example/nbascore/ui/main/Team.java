package com.example.nbascore.ui.main;

public class Team {
    String teamId;
    String name;
    String rank;
    String win;
    String loss;

    public Team(String teamId, String name, String rank, String win, String loss) {
        this.teamId = teamId;
        this.name = name;
        this.rank = rank;
        this.win = win;
        this.loss = loss;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }
}
