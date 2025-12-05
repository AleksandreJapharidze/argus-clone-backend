package com.example.argusclone.dtos.score;

public class CreateScoreRequest {
    private String component;
    private Integer threshold;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setScore(Integer threshold) {
        this.threshold = threshold;
    }
}
