package com.example.argusclone.services.implementation;

import com.example.argusclone.dtos.group.CreateGroupRequest;
import com.example.argusclone.dtos.group.GroupResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Group;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.GroupMapper;
import com.example.argusclone.mappers.LectureMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.LectureRepository;
import com.example.argusclone.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private static final int SEMESTER_WEEKS = 15;

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final GroupMapper groupMapper;
    private final CacheManager cacheManager;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository,
                            CourseRepository courseRepository,
                            LectureRepository lectureRepository,
                            GroupMapper groupMapper,
                            LectureMapper lectureMapper,
                            CacheManager cacheManager) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.groupMapper = groupMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(value = "GROUP_CACHE", key = "'courseId: ' + #courseId")
    public List<GroupResponse> getGroupsForCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course with an id of " + courseId + " not found");
        }

        return groupRepository.findByCourseId(courseId)
                .stream()
                .map(groupMapper::toResponse)
                .toList();
    }

    @Override
    @Cacheable(value = "GROUP_CACHE", key = "'id: ' + #id")
    public GroupResponse getGroupById(Integer id) {
        return groupMapper.toResponse(groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + id + " not found")
        ));
    }

    @Override
    @Caching(evict = @CacheEvict(value = "GROUP_CACHE", key = "'courseId: ' + #courseId"),
            put = {
            @CachePut(value = "GROUP_CACHE", key = "'id: ' + #result.id"),
            @CachePut(value = "GROUP_CACHE", key = "'courseId: ' + #courseId")
    })
    public GroupResponse createGroup(Integer courseId, CreateGroupRequest group) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        Group newGroup = groupMapper.toEntity(group);
        newGroup.setCourse(course);

        Group saved = groupRepository.save(newGroup);
        return groupMapper.toResponse(saved);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "GROUP_CACHE", key = "'id: ' + #id"),
            @CacheEvict(value = "LECTURE_CACHE", key = "'groupId: ' + #id"),
    })
    public void deleteGroupById(Integer id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Group with an id of " + id + " not found")
        );

        Cache cache = cacheManager.getCache("GROUP_CACHE");
        if (cache != null && group.getCourse() != null) {
            cache.evict("courseId: " + group.getCourse().getId());
        }

        groupRepository.delete(group);
    }
}
