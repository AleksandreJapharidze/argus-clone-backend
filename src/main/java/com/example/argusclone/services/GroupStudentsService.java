package com.example.argusclone.services;

import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.student.StudentResponse;

import java.util.List;

public interface GroupStudentsService {
    List<StudentResponse> getStudentsByGroupIdAndCourseId(Integer groupId, Integer courseId);
    GroupResponse assignStudentToGroup(Integer groupId, Integer studentId);
}
