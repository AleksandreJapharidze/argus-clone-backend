package com.example.argusclone.services;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.lecture.CreateLectureRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface GroupService {
    List<GroupResponse> getGroupsForCourse(Integer courseId);
    GroupResponse getGroupById(Integer id);
    GroupResponse createGroup(Integer courseId, CreateGroupRequest group);
    GroupResponse addLecturesToGroup(Integer groupId, List<CreateLectureRequest> lectures);
    void deleteGroupById(Integer id);
    void deleteGroupsByCourseId(Integer courseId);
    void deleteLecturesByCourseId(Integer courseId);
}
