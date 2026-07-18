package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @GetMapping("/{id}/courses")
    public ResponseEntity<Iterable<CourseResponse>> getCoursesByInstructorId(@PathVariable Integer id,
                                                                             @AuthenticationPrincipal Jwt jwt) {
        if ("ROLE_INSTRUCTOR".equals(jwt.getClaimAsString("role"))) {
            Long instructorIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
            if (instructorIdFromToken == null || !id.equals(instructorIdFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }

        return ResponseEntity.ok(courseService.getCoursesByInstructorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<InstructorResponse> addInstructor(@RequestBody CreateInstructorRequest instructor) {
        InstructorResponse savedInstructor = instructorService.addInstructor(instructor);

        URI location = URI.create("/api/v1/instructors/" + savedInstructor.id());
        return ResponseEntity.created(location).body(savedInstructor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructorById(@PathVariable Integer id) {
        instructorService.deleteInstructorById(id);
        return ResponseEntity.noContent().build();
    }
}
