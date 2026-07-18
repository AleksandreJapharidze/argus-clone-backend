package com.example.argusclone.controllers;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureForDay;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.exceptions.NotAMemberException;
import com.example.argusclone.services.GroupStudentsService;
import com.example.argusclone.services.LectureAssignmentService;
import com.example.argusclone.services.LectureService;
import com.example.argusclone.services.LecturesForDayGetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/lectures")
public class GroupLectureController {
    private final LectureService lectureService;
    private final LectureAssignmentService lectureAssignmentService;
    private final LecturesForDayGetterService lecturesForDayGetterService;
    private final GroupStudentsService groupStudentsService;

    public GroupLectureController(LectureService lectureService,
                                  LectureAssignmentService lectureAssignmentService,
                                  LecturesForDayGetterService lecturesForDayGetterService,
                                  GroupStudentsService groupStudentsService) {
        this.lectureService = lectureService;
        this.lectureAssignmentService = lectureAssignmentService;
        this.lecturesForDayGetterService = lecturesForDayGetterService;
        this.groupStudentsService = groupStudentsService;
    }

    @GetMapping
    public ResponseEntity<Iterable<LectureResponse>> getLecturesByGroupId(@PathVariable Integer groupId) {
        return ResponseEntity.ok(lectureService.getLecturesForGroup(groupId));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(params = {"studentId", "lectureDate"})
    public ResponseEntity<Iterable<LectureForDay>> getLecturesForDayForStudent(@PathVariable Integer groupId,
                                                                               @RequestParam Integer studentId,
                                                                               @RequestParam LocalDate lectureDate,
                                                                               @AuthenticationPrincipal Jwt jwt) {
        Long studentIdFromClaim = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
        if (studentIdFromClaim == null || !studentId.equals(studentIdFromClaim.intValue())) {
            return ResponseEntity.status(403).body(null);
        }

        List<Integer> studentGroupIds = groupStudentsService.getAllGroupIdsByStudentId(studentId);
        if (!studentGroupIds.contains(groupId)) {
            throw new NotAMemberException("Student is not a member of the group with id " + groupId);
        }
        return ResponseEntity.ok(lecturesForDayGetterService.getLecturesForDayForStudent(groupId, studentId, lectureDate));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> assignLecturesToGroup(@PathVariable Integer groupId,
                                                        @RequestBody List<CreateLectureRequest> lectures) {
        return ResponseEntity.status(201).body(lectureAssignmentService.generateLecturesForGroup(groupId, lectures));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> deleteLecturesByGroupId(@PathVariable Integer groupId) {
        lectureService.deleteLecturesByGroupId(groupId);
        return ResponseEntity.noContent().build();
    }
}
