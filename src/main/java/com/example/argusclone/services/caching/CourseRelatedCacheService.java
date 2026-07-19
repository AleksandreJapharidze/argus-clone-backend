package com.example.argusclone.services.caching;

import com.example.argusclone.entities.Course;
import com.example.argusclone.services.CourseService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CourseRelatedCacheService {
    private final CourseService courseService;
    private final CacheManager cacheManager;

    public CourseRelatedCacheService(CourseService courseService,
                                     CacheManager cacheManager) {
        this.courseService = courseService;
        this.cacheManager = cacheManager;
    }

    protected void clearAllRelavantCachesForCourse(Integer courseId) {
        Course course = courseService.getCourseById(courseId);
        Cache courseCache = cacheManager.getCache("course-cache");

    }
}
