package com.example.argusclone.services;

import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;

import java.util.List;

public interface SyllabusService {
    SyllabusResponse getSyllabusByCourseId(Integer courseId);
    SyllabusResponse addSyllabusToCourse(Integer courseId, SyllabusRequest syllabus);
    SyllabusResponse updatePrerequisitesByCourseId(Integer courseId, List<String> prerequisites);
    void deleteSyllabusByCourseId(Integer courseId);
}
