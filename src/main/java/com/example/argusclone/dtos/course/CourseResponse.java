package com.example.argusclone.dtos.course;

import com.example.argusclone.dtos.instructor.InstructorResponse;

import java.util.List;

public class CourseResponse {
    private Integer id;
    private String courseName;
    private String courseCode;
    private List<InstructorResponse> instructors;

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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public List<InstructorResponse> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<InstructorResponse> instructors) {
        this.instructors = instructors;
    }
}
