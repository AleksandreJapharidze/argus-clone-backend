package com.example.argusclone.controllers;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.services.ScoreGenerationService;
import com.example.argusclone.services.ScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/scores")
public class CourseScoreController {
    private final ScoreService scoreService;
    private final ScoreGenerationService scoreGenerationService;

    public CourseScoreController(ScoreService scoreService,
                                 ScoreGenerationService scoreGenerationService) {
        this.scoreService = scoreService;
        this.scoreGenerationService = scoreGenerationService;
    }

    @GetMapping
    public ResponseEntity<Iterable<ScoreResponse>> getStudentScoresByCourseId(@PathVariable Integer courseId,
                                                                              @RequestParam Integer studentId) {
        return ResponseEntity.ok(scoreService.getStudentScoresByCourseId(courseId, studentId));
    }

    @PostMapping
    public ResponseEntity<String> generateDefaultScoresForStudentsInCourse(@PathVariable Integer courseId,
                                                                           @RequestBody List<CreateScoreRequest> scores) {
        return ResponseEntity.ok(scoreGenerationService.generateDefaultScoresForStudentsInCourse(courseId, scores));
    }

    @PatchMapping("/{scoreId}")
    public ResponseEntity<ScoreResponse> updateScoreById(@PathVariable Integer courseId,
                                                         @PathVariable Integer scoreId,
                                                         @RequestParam Integer score) {
        return ResponseEntity.ok(scoreService.updateScoreById(courseId, scoreId, score));
    }
}
