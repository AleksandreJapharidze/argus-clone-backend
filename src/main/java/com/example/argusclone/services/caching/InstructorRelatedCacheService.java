package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorRelatedCacheService extends CacheEvictionHelper {
    private final CacheManager cacheManager;
    private final CacheEvictionHelper cacheEvictionHelper;

    public InstructorRelatedCacheService(CacheManager cacheManager,
                                          CacheEvictionHelper cacheEvictionHelper) {
        this.cacheManager = cacheManager;
        this.cacheEvictionHelper = cacheEvictionHelper;
    }

    public void clearAllRelevantCachesForInstructor(Integer instructorId, List<Integer> courseIds) {
        Cache instructorCoursesCache = cacheManager.getCache("instructor-courses-cache");
        Cache courseIdsCache = cacheManager.getCache("instructor-course-ids-cache");
        Cache instructorIdsCache = cacheManager.getCache("instructor-ids-cache");

        cacheEvictionHelper.clearCache(instructorCoursesCache, instructorId);
        cacheEvictionHelper.clearCacheMultipleEntries(courseIdsCache, courseIds);
        cacheEvictionHelper.clearCache(instructorIdsCache, instructorId);
    }
}
