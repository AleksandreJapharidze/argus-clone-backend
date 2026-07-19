package com.example.argusclone.controllers;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.student.StudentResponse;
import com.example.argusclone.services.GroupStudentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @PatchMapping("/{studentId}")
    public ResponseEntity<Void> assignStudentToGroup(@PathVariable Integer groupId,
                                                              @PathVariable Integer studentId,
                                                              @AuthenticationPrincipal Jwt jwt) {
        if ("ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
            Long studentIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
            if (studentIdFromToken == null || !studentId.equals(studentIdFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }

        groupStudentsService.assignStudentToGroup(groupId, studentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> removeStudentFromGroup(@PathVariable Integer groupId,
                                                       @PathVariable Integer studentId,
                                                       @AuthenticationPrincipal Jwt jwt) {
        if ("ROLE_STUDENT".equals(jwt.getClaimAsString("role"))) {
            Long studentIdFromToken = jwt.hasClaim("roleId") ? jwt.getClaim("roleId") : null;
            if (studentIdFromToken == null || !studentId.equals(studentIdFromToken.intValue())) {
                return ResponseEntity.status(403).body(null);
            }
        }
        groupStudentsService.removeStudentFromGroup(groupId, studentId);
        return ResponseEntity.noContent().build();
    }
}
