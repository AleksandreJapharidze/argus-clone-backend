package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorRelatedCacheService extends AbstractCacheService {
    public InstructorRelatedCacheService(CacheManager cacheManager) {
        super(cacheManager);
    }

    public void clearAllRelevantCachesForInstructor(Integer instructorId, List<Integer> courseIds) {
        Cache instructorCoursesCache = cacheManager.getCache("instructor-courses-cache");
        Cache courseIdsCache = cacheManager.getCache("instructor-course-ids-cache");
        Cache instructorIdsCache = cacheManager.getCache("instructor-ids-cache");

        clearCache(instructorCoursesCache, instructorId);
        clearCacheMultipleEntries(courseIdsCache, courseIds);
        clearCache(instructorIdsCache, instructorId);
    }
}
