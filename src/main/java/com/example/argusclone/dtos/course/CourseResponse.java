package com.example.argusclone.dtos.course;

import com.example.argusclone.dtos.instructor.InstructorResponse;

import java.util.List;

public record CourseResponse(Integer id, String courseName, String courseCode, List<InstructorResponse> instructors) {
}
