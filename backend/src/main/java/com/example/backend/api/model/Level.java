package com.example.backend.api.model;

public class Level {

    //team ground goals level
    private String team;
    private String ground;
    private int goals;
    private int level;

    public Level(String team, String ground, int goals, int level) {
        this.team = team;
        this.ground = ground;
        this.goals = goals;
        this.level = level;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "main.java.Level: " + getLevel() + ": " + getTeam() + " at " + getGround();
    }
}
