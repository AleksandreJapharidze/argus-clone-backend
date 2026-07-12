package com.example.argusclone.services;

import com.example.argusclone.dtos.score.CreateScoreRequest;

import java.util.List;

public interface ScoreGenerationService {
    String generateDefaultScoresForStudentsInCourse(Integer courseId, List<CreateScoreRequest> scores);
}
