package com.example.argusclone.dtos.score;

public class CreateScoreRequest {
    private String component;
    private Integer score;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
