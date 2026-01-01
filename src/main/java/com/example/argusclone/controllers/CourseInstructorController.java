package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.services.CourseInstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/instructors")
public class CourseInstructorController {
    private final CourseInstructorService courseInstructorService;

    @Autowired
    public CourseInstructorController(CourseInstructorService courseInstructorService) {
        this.courseInstructorService = courseInstructorService;
    }

    @PatchMapping("/{instructorId}")
    public ResponseEntity<CourseResponse> assignInstructorToCourse(@PathVariable Integer courseId,
                                                                   @PathVariable Integer instructorId) {
        return ResponseEntity.ok(courseInstructorService.assignInstructorToCourse(courseId, instructorId));
    }

    @DeleteMapping("/{instructorId}")
    public ResponseEntity<Void> removeInstructorFromCourse(@PathVariable Integer courseId,
                                                           @PathVariable Integer instructorId) {
        courseInstructorService.removeInstructorFromCourse(courseId, instructorId);
        return ResponseEntity.noContent().build();
    }
}
