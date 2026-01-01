package com.example.argusclone.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String academicStatus;

    @ManyToMany(mappedBy = "students")
    private Set<Group> groups = new HashSet<>();

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

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id != null && id.equals(student.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
