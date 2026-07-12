package com.example.argusclone.controllers;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.services.LectureAssignmentService;
import com.example.argusclone.services.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/lectures")
public class GroupLectureController {
    private final LectureService lectureService;
    private final LectureAssignmentService lectureAssignmentService;

    public GroupLectureController(LectureService lectureService, LectureAssignmentService lectureAssignmentService) {
        this.lectureService = lectureService;
        this.lectureAssignmentService = lectureAssignmentService;
    }

    @GetMapping
    public ResponseEntity<Iterable<LectureResponse>> getLecturesByGroupId(@PathVariable Integer groupId) {
        return ResponseEntity.ok(lectureService.getLecturesForGroup(groupId));
    }

    @PostMapping
    public ResponseEntity<String> assignLecturesToGroup(@PathVariable Integer groupId,
                                                        @RequestBody List<CreateLectureRequest> lectures) {
        return ResponseEntity.status(201).body(lectureAssignmentService.generateLecturesForGroup(groupId, lectures));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLecturesByGroupId(@PathVariable Integer groupId) {
        lectureService.deleteLecturesByGroupId(groupId);
        return ResponseEntity.noContent().build();
    }
}
