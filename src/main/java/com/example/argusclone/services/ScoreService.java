package com.example.argusclone.services;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;

import java.util.List;

public interface ScoreService {
    List<ScoreResponse> getStudentScoresByCourseId(Integer courseId, Integer studentId);
    List<ScoreResponse> generateEmptyListsOfScoresForStudentsByCourseId(Integer courseId, List<CreateScoreRequest> scores);
    ScoreResponse updateScoreById(Integer scoreId, Integer score);
}
