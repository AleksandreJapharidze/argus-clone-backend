package com.example.argusclone.controllers;

import com.example.argusclone.services.CourseDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses/{id}")
public class CourseDeletionController {
    private final CourseDeletionService courseDeletionService;

    @Autowired
    public CourseDeletionController(CourseDeletionService courseDeletionService) {
        this.courseDeletionService = courseDeletionService;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCourseById(@PathVariable Integer id) {
        courseDeletionService.deleteCourseById(id);
        return ResponseEntity.noContent().build();
    }
}
