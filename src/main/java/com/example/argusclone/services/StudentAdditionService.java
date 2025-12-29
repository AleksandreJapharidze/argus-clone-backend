package com.example.argusclone.services;

import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;

public interface StudentAdditionService {
    StudentResponse createStudent(CreateStudentRequest student);
}
