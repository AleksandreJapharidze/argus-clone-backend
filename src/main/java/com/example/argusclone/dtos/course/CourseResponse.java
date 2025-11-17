package com.example.argusclone.dtos.course;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;

import java.util.List;

public class CourseResponse {
    private Integer id;
    private String courseName;
    private String courseCode;
    private SyllabusResponse syllabus;
    private List<InstructorResponse> instructors;
    private List<GroupResponse> groups;

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

    public SyllabusResponse getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(SyllabusResponse syllabus) {
        this.syllabus = syllabus;
    }

    public List<InstructorResponse> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<InstructorResponse> instructors) {
        this.instructors = instructors;
    }

    public List<GroupResponse> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupResponse> groups) {
        this.groups = groups;
    }
}
