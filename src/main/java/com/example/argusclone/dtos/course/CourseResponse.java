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

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public SyllabusResponse getSyllabus() {
        return syllabus;
    }

    public List<InstructorResponse> getInstructors() {
        return instructors;
    }

    public List<GroupResponse> getGroups() {
        return groups;
    }
}
