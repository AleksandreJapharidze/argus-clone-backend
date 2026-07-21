package com.example.argusclone.services;

import com.example.argusclone.dtos.lecture.LectureResponse;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.LectureMapper;
import com.example.argusclone.repositories.GroupRepository;
import com.example.argusclone.repositories.LectureRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final GroupRepository groupRepository;
    private final LectureMapper lectureMapper;

    public LectureService(LectureRepository lectureRepository,
                          GroupRepository groupRepository,
                          LectureMapper lectureMapper) {
        this.lectureRepository = lectureRepository;
        this.groupRepository = groupRepository;
        this.lectureMapper = lectureMapper;
    }

    @Cacheable(cacheNames = "group-lectures-cache", key = "#groupId")
    public List<LectureResponse> getLecturesForGroup(Integer groupId) {
        return lectureRepository.findByGroupId(groupId)
                .stream()
                .map(lectureMapper::toResponse)
                .toList();
    }

    @CacheEvict(cacheNames = "group-lectures-cache", key = "#groupId")
    @Transactional
    public void deleteLecturesByGroupId(Integer groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResourceNotFoundException("Group with an id of " + groupId + " not found");
        }

        lectureRepository.deleteByGroupId(groupId);
    }

}
