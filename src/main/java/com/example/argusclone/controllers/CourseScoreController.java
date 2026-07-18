package com.example.argusclone.controllers;

import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.services.CourseInstructorService;
import com.example.argusclone.services.CourseStudentService;
import com.example.argusclone.services.ScoreGenerationService;
import com.example.argusclone.services.ScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/scores")
public class CourseScoreController {
    private final ScoreService scoreService;
    private final ScoreGenerationService scoreGenerationService;
    private final CourseInstructorService courseInstructorService;
    private final CourseStudentService courseStudentService;

    public CourseScoreController(ScoreService scoreService,
                                 ScoreGenerationService scoreGenerationService,
                                 CourseInstructorService courseInstructorService,
                                 CourseStudentService courseStudentService) {
        this.scoreService = scoreService;
        this.scoreGenerationService = scoreGenerationService;
        this.courseInstructorService = courseInstructorService;
        this.courseStudentService = courseStudentService;
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<Iterable<ScoreResponse>> getStudentScoresByCourseId(@PathVariable Integer courseId,
                                                                              @RequestParam Integer studentId,
                                                                              @AuthenticationPrincipal Jwt jwt) {
        Long studentIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        if (studentIdFromToken == null || !studentId.equals(studentIdFromToken.intValue())) {
            return ResponseEntity.status(403).body(null);
        }
        List<Integer> courseIds = courseStudentService.getCourseIdsByStudentId(studentId);
        if (!courseIds.contains(courseId)) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(scoreService.getStudentScoresByCourseId(courseId, studentId));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping
    public ResponseEntity<String> generateDefaultScoresForStudentsInCourse(@PathVariable Integer courseId,
                                                                           @RequestBody List<CreateScoreRequest> scores,
                                                                           @AuthenticationPrincipal Jwt jwt) {
        Long instructorIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        if (instructorIdFromToken == null) {
            return ResponseEntity.status(403).body(null);
        }
        List<Integer> courseIds = courseInstructorService.getCourseIdsByInstructorId(instructorIdFromToken.intValue());
        if (!courseIds.contains(courseId)) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(scoreGenerationService.generateDefaultScoresForStudentsInCourse(courseId, scores));
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("/{scoreId}")
    public ResponseEntity<ScoreResponse> updateScoreById(@PathVariable Integer courseId,
                                                         @PathVariable Integer scoreId,
                                                         @RequestParam Integer score,
                                                         @AuthenticationPrincipal Jwt jwt) {
        Long instructorIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        if (instructorIdFromToken == null) {
            return ResponseEntity.status(403).body(null);
        }
        List<Integer> courseIds = courseInstructorService.getCourseIdsByInstructorId(instructorIdFromToken.intValue());
        if (!courseIds.contains(courseId)) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(scoreService.updateScoreById(courseId, scoreId, score));
    }
}
