package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorController {
    private final InstructorService instructorService;
    private final CourseService courseService;

    @Autowired
    public InstructorController(InstructorService instructorService, CourseService courseService) {
        this.instructorService = instructorService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorResponse> getInstructorById(@PathVariable Integer id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<Iterable<CourseResponse>> getCoursesByInstructorId(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCoursesByInstructorId(id));
    }

    @PostMapping
    public ResponseEntity<InstructorResponse> addInstructor(@RequestBody CreateInstructorRequest instructor) {
        InstructorResponse savedInstructor = instructorService.addInstructor(instructor);

        URI location = URI.create("/api/v1/instructors/" + savedInstructor.getId());
        return ResponseEntity.created(location).body(savedInstructor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructorById(@PathVariable Integer id) {
        instructorService.deleteInstructorById(id);
        return ResponseEntity.noContent().build();
    }
}
