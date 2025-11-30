package com.example.argusclone.services;

import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;

public interface SyllabusService {
    SyllabusResponse getSyllabusByCourseId(Integer courseId);
    SyllabusResponse addSyllabusToCourse(Integer courseId, SyllabusRequest syllabus);
    void deleteSyllabusByCourseId(Integer courseId);
}
