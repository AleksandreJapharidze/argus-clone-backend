package com.example.argusclone.services;

import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;

import java.util.List;

public interface StudentService {
    StudentResponse getStudentById(Integer id);
    StudentResponse getStudentByName(String name);
    StudentResponse getStudentByEmail(String email);
    List<StudentCourseResultResponse> getStudentCoursesResultsByStudentId(Integer studentId);
    void deleteStudentById(Integer id);
}
