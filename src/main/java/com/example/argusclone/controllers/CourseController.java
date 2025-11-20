package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<CourseResponse> getCourseByName(@RequestParam String courseName) {
        return ResponseEntity.ok(courseService.getCourseByName(courseName));
    }

    @GetMapping(params = "code")
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@RequestParam String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/instructor/{id}")
    public ResponseEntity<Iterable<CourseResponse>> getCoursesByInstructorId(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCoursesByInstructorId(id));
    }

    @PatchMapping("/{courseId}/assign-instructor/{instructorId}")
    public ResponseEntity<CourseResponse> assignInstructorToCourse(@PathVariable Integer courseId, @PathVariable Integer instructorId) {
        return ResponseEntity.ok(courseService.assignInstructorToCourse(courseId, instructorId));
    }
}
