package com.example.argusclone.services;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;

import java.util.List;

public interface CourseService {
    CourseResponse getCourseById(Integer id);
    CourseResponse getCourseByName(String name);
    CourseResponse getCourseByCourseCode(String courseCode);
    List<CourseResponse> getAllCourses();
    List<CourseResponse> getCoursesByInstructorId(Integer instructorId);
    List<CourseResponse> getCoursesByStudentId(Integer studentId);
    SyllabusResponse getCourseSyllabus(Integer courseId);
    CourseResponse addCourse(CreateCourseRequest course);
    SyllabusResponse addCourseSyllabus(Integer courseId, SyllabusRequest syllabus);
    SyllabusResponse updateSyllabusPrerequisites(Integer courseId, List<String> prerequisites);
    void deleteCourseById(Integer id);
    void deleteCourseSyllabus(Integer courseId);
}
