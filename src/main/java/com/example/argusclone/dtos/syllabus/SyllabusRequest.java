package com.example.argusclone.dtos.syllabus;

import com.example.argusclone.entities.embeddable.CourseScheduleCycle;
import com.example.argusclone.entities.embeddable.GradingWeight;
import com.example.argusclone.entities.embeddable.Prerequisite;

import java.util.List;
import java.util.Set;

public record SyllabusRequest(Set<Prerequisite> prerequisites, Set<GradingWeight> gradingWeights, List<CourseScheduleCycle> courseSchedule) {
}
