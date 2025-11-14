package com.example.argusclone.dtos.course;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.syllabus.SyllabusRequest;

import java.util.List;

public class CreateCourseRequest {
    private String courseName;
    private String courseCode;
    private SyllabusRequest syllabus;
    private List<CreateInstructorRequest> instructors;
    private List<CreateGroupRequest> groups;

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

    public SyllabusRequest getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(SyllabusRequest syllabus) {
        this.syllabus = syllabus;
    }

    public List<CreateInstructorRequest> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<CreateInstructorRequest> instructors) {
        this.instructors = instructors;
    }

    public List<CreateGroupRequest> getGroups() {
        return groups;
    }

    public void setGroups(List<CreateGroupRequest> groups) {
        this.groups = groups;
    }
}
