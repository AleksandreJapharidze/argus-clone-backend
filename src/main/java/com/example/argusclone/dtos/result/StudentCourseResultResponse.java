package com.example.argusclone.dtos.result;

public class StudentCourseResultResponse {
    private Integer id;
    private String courseName;
    private String studentName;
    private Boolean hasPassed;
    private Integer finalGrade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Boolean getHasPassed() {
        return hasPassed;
    }

    public void setHasPassed(Boolean hasPassed) {
        this.hasPassed = hasPassed;
    }

    public Integer getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Integer finalGrade) {
        this.finalGrade = finalGrade;
    }
}
