package com.example.argusclone.entities;

import com.example.argusclone.entities.embeddable.CourseScheduleCycle;
import com.example.argusclone.entities.embeddable.GradingWeight;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Syllabus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    @CollectionTable(name = "syllabus_prerequisites", joinColumns = @JoinColumn(name = "syllabus_id"))
    @Column(name = "prerequisites")
    @BatchSize(size = 20)
    private List<String> prerequisites;

    private String courseMission;

    @ElementCollection
    @CollectionTable(name = "syllabus_teaching_methods", joinColumns = @JoinColumn(name = "syllabus_id"))
    @Column(name = "method")
    @BatchSize(size = 20)
    private List<String> teachingMethods;

    @ElementCollection
    @CollectionTable(name = "syllabus_topics", joinColumns = @JoinColumn(name = "syllabus_id"))
    @Column(name = "topic")
    @BatchSize(size = 20)
    private List<String> topics;

    @ElementCollection
    @CollectionTable(name = "syllabus_grading_weights", joinColumns = @JoinColumn(name = "syllabus_id"))
    @OrderColumn(name = "position")
    @BatchSize(size = 20)
    private List<GradingWeight> gradingWeights = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "syllabus_course_schedule", joinColumns = @JoinColumn(name = "syllabus_id"))
    @OrderColumn(name = "position")
    @BatchSize(size = 20)
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
