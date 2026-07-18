package com.example.argusclone.controllers;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable  Integer id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable Integer id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.noContent().build();
    }
}
