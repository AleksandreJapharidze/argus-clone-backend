package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.dtos.score.CreateScoreRequest;
import com.example.argusclone.dtos.score.ScoreResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;
    private final ScoreService scoreService;

    @Autowired
    public CourseController(CourseService courseService, ScoreService scoreService) {
        this.courseService = courseService;
        this.scoreService = scoreService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<CourseResponse>> getAllCourses(@RequestParam int pageNumber,
                                                                  @RequestParam int pageSize) {
        return ResponseEntity.ok(courseService.getAllCourses(PageRequest.of(pageNumber-1, pageSize)));
    }

    @GetMapping("/search")
    public ResponseEntity<Iterable<CourseResponse>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchCourses(keyword));
    }

    @GetMapping("/{courseId}/scores")
    public ResponseEntity<Iterable<ScoreResponse>> getStudentScoresByCourseId(@PathVariable Integer courseId,
                                                                               @RequestParam Integer studentId) {
        return ResponseEntity.ok(scoreService.getStudentScoresByCourseId(courseId, studentId));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> addCourse(@RequestBody CreateCourseRequest course) {
        CourseResponse savedCourse = courseService.addCourse(course);

        URI location = URI.create("/api/v1/courses/" + savedCourse.id());
        return ResponseEntity.created(location).body(savedCourse);
    }

    @PostMapping("/{courseId}/scores")
    public ResponseEntity<Iterable<ScoreResponse>> generateEmptyListOfScoresForStudentsByCourseId(@PathVariable Integer courseId,
                                                                                                  @RequestBody List<CreateScoreRequest> scores) {
        return ResponseEntity.ok(scoreService.generateEmptyListsOfScoresForStudentsByCourseId(courseId, scores));
    }
}
