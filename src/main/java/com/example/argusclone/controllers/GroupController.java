package com.example.argusclone.controllers;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.services.GroupService;
import com.example.argusclone.services.GroupStudentsService;
import com.example.argusclone.services.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;
    private final GroupStudentsService groupStudentsService;
    private final LectureService lectureService;

    @Autowired
    public GroupController(GroupService groupService,
                           GroupStudentsService groupStudentsService,
                           LectureService lectureService) {
        this.groupService = groupService;
        this.groupStudentsService = groupStudentsService;
        this.lectureService = lectureService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable  Integer id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping("/{id}/lectures")
    public ResponseEntity<Iterable<LectureResponse>> getLecturesByGroupId(@PathVariable Integer id) {
        return ResponseEntity.ok(lectureService.getLecturesForGroup(id));
    }

    @PostMapping("/{groupId}/lectures")
    public ResponseEntity<Iterable<LectureResponse>> assignLecturesToGroup(@PathVariable Integer groupId,
                                                               @RequestBody List<CreateLectureRequest> lectures) {
        List<LectureResponse> savedLectures = lectureService.addLecturesToGroup(groupId, lectures);

        URI location = URI.create("/api/v1/groups/" + groupId + "/lectures");
        return ResponseEntity.created(location).body(savedLectures);
    }

    @PatchMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<GroupResponse> assignStudentToGroup(@PathVariable Integer groupId,
                                                              @PathVariable Integer studentId) {
        return ResponseEntity.ok(groupStudentsService.assignStudentToGroup(groupId, studentId));
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromGroup(@PathVariable Integer groupId,
                                                       @PathVariable Integer studentId) {
        groupStudentsService.removeStudentFromGroup(groupId, studentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupById(@PathVariable Integer id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}/lectures")
    public ResponseEntity<Void> deleteLecturesByGroupId(@PathVariable Integer groupId) {
        lectureService.deleteLecturesByGroupId(groupId);
        return ResponseEntity.noContent().build();
    }
}
