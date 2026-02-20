package com.example.argusclone.services;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.student.StudentResponse;

import java.util.List;

public interface GroupStudentsService {
    List<StudentResponse> getStudentsByGroupId(Integer groupId);
    GroupResponse assignStudentToGroup(Integer groupId, Integer studentId);
    void removeStudentFromGroup(Integer groupId, Integer studentId);
}
