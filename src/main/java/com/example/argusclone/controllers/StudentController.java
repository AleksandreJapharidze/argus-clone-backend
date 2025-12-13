package com.example.argusclone.controllers;

import com.example.argusclone.dtos.course.CourseResponse;
import com.example.argusclone.dtos.result.StudentCourseResultResponse;
import com.example.argusclone.dtos.student.CreateStudentRequest;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.services.CourseService;
import com.example.argusclone.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<Iterable<CourseResponse>> getCoursesByStudentId(@PathVariable Integer id) {
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

    @GetMapping("/{studentId}/coursesResults")
    public ResponseEntity<Iterable<StudentCourseResultResponse>> getStudentCourseResults(@PathVariable Integer studentId) {
        return ResponseEntity.ok(studentService.getStudentCoursesResultsByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> addStudent(@RequestBody CreateStudentRequest student) {
        StudentResponse savedStudent = studentService.addStudent(student);

        URI location = URI.create("/api/v1/students/" + savedStudent.getId());
        return ResponseEntity.created(location).body(savedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentById(@PathVariable Integer id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }
}
