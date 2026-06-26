package com.example.argusclone.controllers;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/groups")
public class CourseGroupController {
    private final GroupService groupService;

    @Autowired
    public CourseGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<Iterable<GroupResponse>> getGroupsForCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(groupService.getGroupsForCourse(courseId));
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@PathVariable Integer courseId,
                                                     @RequestBody CreateGroupRequest group) {
        return ResponseEntity.status(201).body(groupService.createGroup(courseId, group));
    }
}
