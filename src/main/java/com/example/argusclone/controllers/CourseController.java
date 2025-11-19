package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/id/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/name/{courseName}")
    public ResponseEntity<CourseResponse> getCourseByName(@PathVariable String courseName) {
        return ResponseEntity.ok(courseService.getCourseByName(courseName));
    }

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<CourseResponse> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }


}
