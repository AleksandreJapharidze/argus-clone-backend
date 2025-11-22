package com.example.argusclone.controllers;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable  Integer id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping("/{id}/lectures")
    public ResponseEntity<Iterable<LectureResponse>> getLecturesByGroupId(@PathVariable Integer id) {
        return ResponseEntity.ok(groupService.getLecturesForGroup(id));
    }

    @PostMapping("/{groupId}/lectures")
    public ResponseEntity<GroupResponse> assignLecturesToGroup(@PathVariable Integer groupId, @RequestBody List<CreateLectureRequest> lectures) {
        GroupResponse savedLectures = groupService.addLecturesToGroup(groupId, lectures);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/groups/" + groupId + "/lectures")
                .buildAndExpand(groupId)
                .toUri();
        return ResponseEntity.created(location).body(savedLectures);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable Integer id) {
        groupService.deleteLecturesByGroupId(id);
        groupService.deleteGroupById(id);
        return ResponseEntity.noContent().build();
    }
}
