package com.example.argusclone.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String academicStatus;

    @ManyToMany(mappedBy = "students")
    private List<Group> groups;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Score> scores;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentCourseResult> studentCourseResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public List<StudentCourseResult> getStudentCourseResults() {
        return studentCourseResults;
    }

    public void setStudentCourseResults(List<StudentCourseResult> studentCourseResults) {
        this.studentCourseResults = studentCourseResults;
    }
}
