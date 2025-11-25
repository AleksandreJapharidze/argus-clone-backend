package com.example.argusclone.services;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;

public interface StudentService {
    StudentResponse getStudentById(Integer id);
    StudentResponse getStudentByName(String name);
    StudentResponse getStudentByEmail(String email);
    StudentResponse addStudent(CreateStudentRequest student);
    ScoreResponse addScoreToStudent(Integer studentId, Integer courseId, CreateScoreRequest score);
    void deleteStudentById(Integer id);
}
