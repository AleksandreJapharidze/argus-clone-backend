package com.example.argusclone.services;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;

public interface StudentService {
    StudentResponse getStudentById(Integer id);
    StudentResponse getStudentByName(String name);
    StudentResponse getStudentByEmail(String email);
    StudentResponse addStudent(CreateStudentRequest student);
    void deleteStudentById(Integer id);
}
