package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.StudentAdditionDeletionService;
import com.example.argusclone.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final StudentAdditionDeletionService studentAdditionDeletionService;
    private final CourseService courseService;

    @Autowired
    public StudentController(StudentService studentService,
                             StudentAdditionDeletionService studentAdditionDeletionService,
                             CourseService courseService) {
        this.studentService = studentService;
        this.studentAdditionDeletionService = studentAdditionDeletionService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/{id}/courses")
    public ResponseEntity<Iterable<CourseResponse>> getCoursesByStudentId(@PathVariable Integer id,
                                                                          @AuthenticationPrincipal Jwt jwt) {
        if ("ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
            Long studentIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
            if (studentIdFromToken == null || !id.equals(studentIdFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }

        return ResponseEntity.ok(courseService.getCoursesByStudentId(id));
    }

    @GetMapping(params = "name")
    public ResponseEntity<StudentResponse> getStudentByName(@RequestParam String name) {
        return ResponseEntity.ok(studentService.getStudentByName(name));
    }

    @GetMapping(params = "email")
    public ResponseEntity<StudentResponse> getStudentByEmail(@RequestParam String email) {
        return ResponseEntity.ok(studentService.getStudentByEmail(email));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/{studentId}/courses-results")
    public ResponseEntity<Iterable<StudentCourseResultResponse>> getStudentCourseResults(@PathVariable Integer studentId,
                                                                                         @AuthenticationPrincipal Jwt jwt) {
        if ("ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
            Long studentIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
            if (studentIdFromToken == null || !studentId.equals(studentIdFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }

        return ResponseEntity.ok(studentService.getStudentCoursesResultsByStudentId(studentId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(@RequestBody CreateStudentRequest student) {
        StudentResponse savedStudent = studentAdditionDeletionService.createStudent(student);

        URI location = URI.create("/api/v1/students/" + savedStudent.id());
        return ResponseEntity.created(location).body(savedStudent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Integer id) {
        studentAdditionDeletionService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }
}
