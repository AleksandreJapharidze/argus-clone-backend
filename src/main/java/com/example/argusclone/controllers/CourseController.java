package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.course.CreateCourseRequest;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CourseResponse> addCourse(@RequestBody CreateCourseRequest course) {
        return ResponseEntity.status(201).body(courseService.addCourse(course));
    }
}
