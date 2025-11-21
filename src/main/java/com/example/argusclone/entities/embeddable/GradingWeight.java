package com.example.argusclone.entities.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class GradingWeight {
    private String component;
    private Integer weight;
    private Integer threshold;

    public GradingWeight() {
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }
}
