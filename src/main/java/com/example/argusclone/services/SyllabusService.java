package com.example.argusclone.services;

import com.example.argusclone.dtos.syllabus.SyllabusRequest;
import com.example.argusclone.dtos.syllabus.SyllabusResponse;
import com.example.argusclone.entities.embeddable.GradingWeight;

import java.util.List;
import java.util.Set;

public interface SyllabusService {
    SyllabusResponse getSyllabusByCourseId(Integer courseId);
    SyllabusResponse addSyllabusToCourse(Integer courseId, SyllabusRequest syllabus);
    SyllabusResponse updatePrerequisitesByCourseId(Integer courseId, Set<String> prerequisites);
    SyllabusResponse updateCourseMissionByCourseId(Integer courseId, String courseMission);
    SyllabusResponse updateTeachingMethodsByCourseId(Integer courseId, List<String> teachingMethods);
    SyllabusResponse updateTopicsByCourseId(Integer courseId, List<String> topics);
    SyllabusResponse updateGradingWeightsByCourseId(Integer courseId, List<GradingWeight> gradingWeights);
}
