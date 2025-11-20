package com.example.argusclone.services;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;

import java.util.List;

public interface CourseService {
    CourseResponse getCourseById(Integer id);
    CourseResponse getCourseByName(String name);
    CourseResponse getCourseByCourseCode(String courseCode);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesByInstructorId(Integer instructorId);
    CourseResponse addCourse(CreateCourseRequest course);
    CourseResponse assignInstructorToCourse(Integer courseId, Integer instructorId);
    void deleteCourseById(Integer id);
}
