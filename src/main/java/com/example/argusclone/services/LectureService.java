package com.example.argusclone.services;

import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;

import java.time.LocalDate;
import java.util.List;

public interface LectureService {
    List<LectureResponse> getLecturesForGroup(Integer groupId);
    List<LectureResponse> getLecturesByLectureDateForStudent(Integer studentId, LocalDate lectureDate);
    List<LectureResponse> addLecturesToGroup(Integer groupId, List<CreateLectureRequest> lectures);
    void deleteLecturesByGroupId(Integer groupId);
    void deleteLecturesByCourseId(Integer courseId);
}
