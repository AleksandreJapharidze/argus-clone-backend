package com.example.argusclone.services.implementation;

import com.example.argusclone.repositories.StudentCourseResultRepository;
import com.example.argusclone.services.CourseDeletionService;
import com.example.argusclone.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeletionServiceImpl implements CourseDeletionService {
    private final CourseService courseService;
    private final StudentCourseResultRepository studentCourseResultRepository;

    @Autowired
    public CourseDeletionServiceImpl(CourseService courseService, StudentCourseResultRepository studentCourseResultRepository) {
        this.courseService = courseService;
        this.studentCourseResultRepository = studentCourseResultRepository;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "STUDENT_COURSE_RESULTS_CACHE", allEntries = true),
            @CacheEvict(value = "GROUP_CACHE", allEntries = true),
            @CacheEvict(value = "LECTURE_CACHE", allEntries = true),
            @CacheEvict(value = "SCORE_CACHE", allEntries = true),
    })
    public void deleteCourseById(Integer id) {
        studentCourseResultRepository.deleteByCourseId(id);
        courseService.deleteCourseById(id);
    }
}
