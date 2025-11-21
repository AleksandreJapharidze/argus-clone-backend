package com.example.argusclone.entities.embeddable;

import jakarta.persistence.Embeddable;

@Embeddable
public class CourseScheduleCycle {
    private Integer week;
    private String activities;

    public CourseScheduleCycle() {
    }

    public Integer getWeek() {
        return week;
    }
    public void setWeek(Integer week) {
        this.week = week;
    }
    public String getActivities() {
        return activities;
    }
    public void setActivities(String activities) {
        this.activities = activities;
    }
}
