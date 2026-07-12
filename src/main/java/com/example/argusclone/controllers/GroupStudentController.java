package com.example.argusclone.controllers;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.services.GroupStudentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/students")
public class GroupStudentController {
    private final GroupStudentsService groupStudentsService;

    public GroupStudentController(GroupStudentsService groupStudentsService) {
        this.groupStudentsService = groupStudentsService;
    }

    @GetMapping
    public ResponseEntity<Iterable<StudentResponse>> getStudentsInGroup(@PathVariable Integer groupId) {
        return ResponseEntity.ok(groupStudentsService.getStudentsByGroupId(groupId));
    }

    @PatchMapping("/{studentId}")
    public ResponseEntity<GroupResponse> assignStudentToGroup(@PathVariable Integer groupId,
                                                              @PathVariable Integer studentId) {
        return ResponseEntity.ok(groupStudentsService.assignStudentToGroup(groupId, studentId));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> removeStudentFromGroup(@PathVariable Integer groupId,
                                                       @PathVariable Integer studentId) {
        groupStudentsService.removeStudentFromGroup(groupId, studentId);
        return ResponseEntity.noContent().build();
    }
}
