package com.example.argusclone.services;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;

import java.util.List;

public interface LectureAssignmentService {
    String generateLecturesForGroup(Integer groupId, List<CreateLectureRequest> lectures);
}
