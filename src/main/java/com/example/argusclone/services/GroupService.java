package com.example.argusclone.services;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Group;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final LectureRepository lectureRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository,
                        CourseRepository courseRepository,
                        LectureRepository lectureRepository,
                        GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.lectureRepository = lectureRepository;
        this.groupMapper = groupMapper;
    }

    public List<GroupResponse> getGroupsForCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course with an id of " + courseId + " not found");
        }

        return groupRepository.findByCourseId(courseId)
                .stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    public GroupResponse getGroupById(Integer id) {
        return groupMapper.toResponse(groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + id + " not found")
        ));
    }

    public GroupResponse createGroup(Integer courseId, CreateGroupRequest group) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Group newGroup = groupMapper.toEntity(group);
        newGroup.setCourse(course);

        Group saved = groupRepository.save(newGroup);
        return groupMapper.toResponse(saved);
    }

    public void deleteGroupById(Integer id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + id + " not found")
        );

        lectureRepository.deleteByGroupId(id);
        groupRepository.delete(group);
    }
}
