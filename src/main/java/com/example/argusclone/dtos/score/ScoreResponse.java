package com.example.argusclone.dtos.score;

public record ScoreResponse(Integer id, String component, Integer score, Integer maxScore, Integer threshold, String courseName, String studentName) {
}
