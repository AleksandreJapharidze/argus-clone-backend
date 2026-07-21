package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CourseRelatedCacheService extends AbstractCacheService {
    public CourseRelatedCacheService(CacheManager cacheManager) {
        super(cacheManager);
    }

    public void clearAllRelevantCachesForCourse(Integer courseId) {
        Cache courseCache = cacheManager.getCache("course-cache");
        Cache instructorIdsCache = cacheManager.getCache("instructor-ids-cache");
        Cache syllabusCache = cacheManager.getCache("syllabus-cache");

        clearCache(courseCache, courseId);
        clearCache(instructorIdsCache, courseId);
        clearCache(syllabusCache, courseId);
    }
}
