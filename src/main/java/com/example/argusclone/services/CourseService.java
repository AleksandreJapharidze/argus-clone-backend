package com.example.argusclone.services;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    CourseResponse getCourseById(Integer id);
    List<CourseResponse> getAllCourses(Pageable pageable);
    List<CourseResponse> searchCourses(String keyword);
    List<CourseResponse> getCoursesByInstructorId(Integer instructorId);
    List<CourseResponse> getCoursesByStudentId(Integer studentId);
    CourseResponse addCourse(CreateCourseRequest course);
    void deleteCourseById(Integer id);
    void deleteStudentCourseResultByCourseId(Integer courseId);
}
