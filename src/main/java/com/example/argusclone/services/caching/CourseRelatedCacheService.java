package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CourseRelatedCacheService {
    private final CacheManager cacheManager;
    private final CacheEvictionHelper cacheEvictionHelper;

    public CourseRelatedCacheService(CacheManager cacheManager,
                                     CacheEvictionHelper cacheEvictionHelper) {
        this.cacheManager = cacheManager;
        this.cacheEvictionHelper = cacheEvictionHelper;
    }

    public void clearAllRelevantCachesForCourse(Integer courseId) {
        Cache courseCache = cacheManager.getCache("course-cache");
        Cache instructorIdsCache = cacheManager.getCache("instructor-ids-cache");
        Cache syllabusCache = cacheManager.getCache("syllabus-cache");

        cacheEvictionHelper.clearCache(courseCache, courseId);
        cacheEvictionHelper.clearCache(instructorIdsCache, courseId);
        cacheEvictionHelper.clearCache(syllabusCache, courseId);
    }
}
