package com.example.argusclone.controllers;

import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.services.GroupStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/students")
public class GroupStudentsGetterController {
    private final GroupStudentsService groupStudentsService;

    @Autowired
    public GroupStudentsGetterController(GroupStudentsService groupStudentsService) {
        this.groupStudentsService = groupStudentsService;
    }

    @GetMapping
    public ResponseEntity<Iterable<StudentResponse>> getStudentsInGroup(@PathVariable Integer groupId) {
        return ResponseEntity.ok(groupStudentsService.getStudentsByGroupId(groupId));
    }
}
