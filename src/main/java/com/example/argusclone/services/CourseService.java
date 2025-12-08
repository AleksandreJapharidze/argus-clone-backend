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
    List<CourseResponse> getCoursesByStudentId(Integer studentId);
    CourseResponse addCourse(CreateCourseRequest course);
    void deleteCourseById(Integer id);
    void deleteCourseSyllabus(Integer courseId);
}
