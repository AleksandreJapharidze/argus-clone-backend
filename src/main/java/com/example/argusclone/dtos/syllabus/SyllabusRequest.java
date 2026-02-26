package com.example.argusclone.dtos.syllabus;

import com.example.argusclone.entities.embeddable.CourseScheduleCycle;
import com.example.argusclone.entities.embeddable.GradingWeight;

import java.util.List;

public record SyllabusRequest(List<String> prerequisites, String courseMission, List<String> teachingMethods, List<String> topics,
                              List<GradingWeight> gradingWeights, List<CourseScheduleCycle> courseSchedule) {
}
