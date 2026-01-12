package com.example.argusclone.services;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getGroupsForCourse(Integer courseId);
    GroupResponse getGroupById(Integer id);
    GroupResponse createGroup(Integer courseId, CreateGroupRequest group);
    void deleteGroupById(Integer id);
    void deleteGroupsByCourseId(Integer courseId);
}
