package com.example.argusclone.services;

import com.example.argusclone.entities.Course;
import com.example.argusclone.exceptions.OperationNotAllowedYetException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.services.caching.CourseRelatedCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseDeletionService {
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final CourseRelatedCacheService courseRelatedCacheService;

    @Autowired
    public CourseDeletionService(CourseService courseService,
                                 CourseRepository courseRepository,
                                 CourseRelatedCacheService courseRelatedCacheService) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.courseRelatedCacheService = courseRelatedCacheService;
    }

    @Transactional
    public void deleteCourseById(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + id + " not found")
        );

        if (course.getGroups() != null || !course.getGroups().isEmpty()) {
            throw new OperationNotAllowedYetException("Course can't be deleted because it has active groups. Delete them first.");
        }

        if (course.getScores() != null || !course.getScores().isEmpty()) {
            throw new OperationNotAllowedYetException("Course can't be deleted because it has active scores. Delete them first.");
        }

        courseRelatedCacheService.clearAllRelevantCachesForCourse(id);
        courseService.deleteCourseById(id);
    }
}
