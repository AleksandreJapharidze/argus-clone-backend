package com.example.argusclone.dtos.syllabus;

import java.util.List;
import java.util.Map;

public class SyllabusResponse {
    private Integer id;
    private List<String> prerequisites;
    private String courseMission;
    private List<String> teachingMethods;
    private List<String> topics;
    private Map<String, Integer> gradingWeights;
    private Map<String, String> courseSchedule;

    public Integer getId() {
        return id;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public String getCourseMission() {
        return courseMission;
    }

    public List<String> getTeachingMethods() {
        return teachingMethods;
    }

    public List<String> getTopics() {
        return topics;
    }

    public Map<String, Integer> getGradingWeights() {
        return gradingWeights;
    }

    public Map<String, String> getCourseSchedule() {
        return courseSchedule;
    }
}
