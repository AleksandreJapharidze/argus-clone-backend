package com.example.argusclone.services;

import com.example.argusclone.dtos.group.GroupResponse;

public interface StudentGroupAssignmentService {
    GroupResponse assignStudentToGroup(Integer groupId, Integer studentId);
}
