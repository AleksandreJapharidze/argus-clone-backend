package com.example.argusclone.services;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.dtos.lecture.CreateLectureRequest;
import com.example.argusclone.dtos.lecture.LectureResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getGroupsForCourse(Integer courseId);
    GroupResponse getGroupById(Integer id);
    List<LectureResponse> getLecturesForGroup(Integer groupId);
    GroupResponse createGroup(Integer courseId, CreateGroupRequest group);
    GroupResponse addLecturesToGroup(Integer groupId, List<CreateLectureRequest> lectures);
    void deleteGroupById(Integer id);
    void deleteLecturesByGroupId(Integer groupId);
    void deleteGroupsByCourseId(Integer courseId);
}
