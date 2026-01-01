package com.example.argusclone.services;

import com.example.argusclone.dtos.course.CourseResponse;

public interface CourseInstructorService {
    CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId);
    void removeInstructorFromCourse(Integer courseId, Integer instructorId);
}
