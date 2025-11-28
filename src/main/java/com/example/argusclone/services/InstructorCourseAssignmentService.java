package com.example.argusclone.services;

import com.example.argusclone.dtos.course.CourseResponse;

public interface InstructorCourseAssignmentService {
    CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId);
}
