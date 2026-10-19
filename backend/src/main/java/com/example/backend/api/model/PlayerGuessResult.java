package com.example.backend.api.model;

public class PlayerGuessResult {

    private String fullName;

    public PlayerGuessResult(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
