package com.example.argusclone.entities;

import com.example.argusclone.entities.embeddable.CourseScheduleCycle;
import com.example.argusclone.entities.embeddable.GradingWeight;
import com.example.argusclone.entities.embeddable.Prerequisite;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Syllabus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL)
    private Set<Prerequisite> prerequisites = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "syllabus_grading_weights", joinColumns = @JoinColumn(name = "syllabus_id"))
    @OrderColumn(name = "grading_weight_order")
    @BatchSize(size = 50)
    private List<GradingWeight> gradingWeights = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "syllabus_course_schedule", joinColumns = @JoinColumn(name = "syllabus_id"))
    @OrderColumn(name = "cycle_order")
    @BatchSize(size = 50)
    private List<CourseScheduleCycle> courseSchedule = new ArrayList<>();

    @OneToOne(mappedBy = "syllabus")
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public Integer getId() {
        return id;
    }

    public Set<Prerequisite> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(Set<Prerequisite> prerequisites) {
        this.prerequisites = prerequisites;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
