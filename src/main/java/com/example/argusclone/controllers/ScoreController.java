package com.example.argusclone.controllers;

import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scores")
public class ScoreController {
    private final ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScoreResponse> updateScoreById(@PathVariable Integer id, @RequestParam Integer score) {
        return ResponseEntity.ok(scoreService.updateScoreById(id, score));
    }
}
