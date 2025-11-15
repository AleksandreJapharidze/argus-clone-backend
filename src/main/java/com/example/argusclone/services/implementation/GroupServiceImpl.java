package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.entities.Group;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.services.GroupService;

import java.util.List;

public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;
    private CourseRepository courseRepository;
    private GroupMapper groupMapper;

    @Override
    public List<GroupResponse> getGroupsForCourse(Integer courseId) {
        return groupRepository.findByCourseId(courseId)
                .stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    @Override
    public GroupResponse getGroupById(Integer id) {
        Group group = groupRepository.findById(id).orElseThrow();
        return groupMapper.toResponse(group);
    }

    @Override
    public GroupResponse createGroup(Integer courseId, CreateGroupRequest group) {
        return null;
    }

    @Override
    public void deleteGroupById(Integer id) {
        if (!groupRepository.existsById(id)) {
            return;
        }
        groupRepository.deleteById(id);
    }
}
