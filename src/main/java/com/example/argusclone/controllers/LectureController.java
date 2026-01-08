package com.example.argusclone.controllers;

import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.services.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/lectures")
public class LectureController {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public ResponseEntity<Iterable<LectureResponse>> getLecturesByLectureDateForStudent(@RequestParam Integer studentId,
                                                                                        @RequestParam LocalDate lectureDate) {
        return ResponseEntity.ok(lectureService.getLecturesByLectureDateForStudent(studentId, lectureDate));
    }
}
