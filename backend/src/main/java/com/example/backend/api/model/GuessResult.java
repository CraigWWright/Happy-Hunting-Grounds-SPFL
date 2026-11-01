package com.example.backend.api.model;

public class GuessResult {

    //2=correct 1=wrong season 0=wrong
    private int correct;
    private double points;
    private String date;
    private String homeTeam;
    private String awayTeam;
    private String finalScore;
    private String playerGoalsAt;
    private String homeScorers;
    private String awayScorers;


    // incorrect guess — no match details needed
    public GuessResult(int correct, double points) {
        this.correct = correct;
        this.points = points;
    }

    // correct guess — full match details
    public GuessResult(int correct, double points, String date, String homeTeam,
                       String awayTeam, String finalScore, String playerGoalsAt,
                       String homeScorers, String awayScorers) {
        this.correct = correct;
        this.points = points;
        this.date = date;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.finalScore = finalScore;
        this.playerGoalsAt = playerGoalsAt;
        this.homeScorers = homeScorers;
        this.awayScorers = awayScorers;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getPlayerGoalsAt() {
        return playerGoalsAt;
    }

    public void setPlayerGoalsAt(String playerGoalsAt) {
        this.playerGoalsAt = playerGoalsAt;
    }

    public String getHomeScorers() {
        return homeScorers;
    }

    public void setHomeScorers(String homeScorers) {
        this.homeScorers = homeScorers;
    }

    public String getAwayScorers() {
        return awayScorers;
    }

    public void setAwayScorers(String awayScorers) {
        this.awayScorers = awayScorers;
    }
}
