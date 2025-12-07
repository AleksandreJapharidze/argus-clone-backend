package com.example.argusclone.dtos.syllabus;

import com.example.argusclone.entities.embeddable.CourseScheduleCycle;
import com.example.argusclone.entities.embeddable.GradingWeight;

import java.util.ArrayList;
import java.util.List;

public class SyllabusResponse {
    private Integer id;
    private List<String> prerequisites;
    private String courseMission;
    private List<String> teachingMethods;
    private List<String> topics;
    private List<GradingWeight> gradingWeights = new ArrayList<>();
    private List<CourseScheduleCycle> courseSchedule = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<GradingWeight> getGradingWeights() {
        return gradingWeights;
    }

    public void setGradingWeights(List<GradingWeight> gradingWeights) {
        this.gradingWeights = gradingWeights;
    }

    public List<CourseScheduleCycle> getCourseSchedule() {
        return courseSchedule;
    }

    public void setCourseSchedule(List<CourseScheduleCycle> courseSchedule) {
        this.courseSchedule = courseSchedule;
    }
}
