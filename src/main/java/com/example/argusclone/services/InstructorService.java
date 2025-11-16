package com.example.argusclone.services;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;

public interface InstructorService {
    InstructorResponse getInstructorById(Integer id);
    InstructorResponse getInstructorByName(String name);
    InstructorResponse getInstructorByEmail(String email);
    InstructorResponse addInstructor(CreateInstructorRequest instructor);
    void deleteInstructorById(Integer id);
}
