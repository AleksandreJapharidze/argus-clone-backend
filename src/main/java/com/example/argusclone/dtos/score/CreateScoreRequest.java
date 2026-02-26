package com.example.argusclone.dtos.score;

public record CreateScoreRequest(String component, Integer maxScore, Integer threshold) {
}
