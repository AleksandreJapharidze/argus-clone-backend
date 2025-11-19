package com.example.argusclone.controllers;

import com.example.argusclone.dtos.instructor.CreateInstructorRequest;
import com.example.argusclone.dtos.instructor.InstructorResponse;
import com.example.argusclone.mappers.InstructorMapper;
import com.example.argusclone.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @Autowired
    private InstructorMapper instructorMapper;

    @GetMapping("/id/{id}")
    public ResponseEntity<InstructorResponse> getInstructorById(@PathVariable Integer id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<InstructorResponse> getInstructorByName(@PathVariable String name) {
        return ResponseEntity.ok(instructorService.getInstructorByName(name));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<InstructorResponse> getInstructorByEmail(@PathVariable String email) {
        return ResponseEntity.ok(instructorService.getInstructorByEmail(email));
    }

    @PostMapping
    public ResponseEntity<InstructorResponse> addInstructor(@RequestBody CreateInstructorRequest instructor) {
        return ResponseEntity.ok(instructorService.addInstructor(instructor));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteInstructorById(@PathVariable Integer id) {
        instructorService.deleteInstructorById(id);
        return ResponseEntity.noContent().build();
    }
}
