package com.example.argusclone.dtos.syllabus;

import java.util.List;
import java.util.Map;

public class SyllabusRequest {
    private List<String> prerequisites;
    private String courseMission;
    private List<String> teachingMethods;
    private List<String> topics;
    private Map<String, Integer> gradingWeights;
    private Map<String, String> courseSchedule;

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getCourseMission() {
        return courseMission;
    }

    public void setCourseMission(String courseMission) {
        this.courseMission = courseMission;
    }

    public List<String> getTeachingMethods() {
        return teachingMethods;
    }

    public void setTeachingMethods(List<String> teachingMethods) {
        this.teachingMethods = teachingMethods;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Map<String, Integer> getGradingWeights() {
        return gradingWeights;
    }

    public void setGradingWeights(Map<String, Integer> gradingWeights) {
        this.gradingWeights = gradingWeights;
    }

    public Map<String, String> getCourseSchedule() {
        return courseSchedule;
    }

    public void setCourseSchedule(Map<String, String> courseSchedule) {
        this.courseSchedule = courseSchedule;
    }
}
