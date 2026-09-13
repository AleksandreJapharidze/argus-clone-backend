package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentRelatedCacheService extends CacheEvictionHelper {
    private final CacheManager cacheManager;
    private final CacheEvictionHelper cacheEvictionHelper;

    public StudentRelatedCacheService(CacheManager cacheManager,
                                       CacheEvictionHelper cacheEvictionHelper) {
        this.cacheManager = cacheManager;
        this.cacheEvictionHelper = cacheEvictionHelper;
    }

    public void clearAllRelevantCachesForStudent(Integer studentId, List<Integer> groupIds, List<Integer> courseIds) {
        Cache studentCoursesCache = cacheManager.getCache("student-courses-cache");
        Cache studentCourseIdsCache = cacheManager.getCache("student-course-ids-cache");
        Cache studentGroupIdsCache = cacheManager.getCache("student-group-ids-cache");
        Cache studentCourseScoresCache = cacheManager.getCache("student-course-scores-cache");
        Cache studentCourseResultsCache = cacheManager.getCache("student-course-results-cache");
        Cache groupStudentsCache = cacheManager.getCache("group-students-cache");

        cacheEvictionHelper.clearCache(studentCoursesCache, studentId);
        cacheEvictionHelper.clearCache(studentCourseIdsCache, studentId);
        cacheEvictionHelper.clearCache(studentGroupIdsCache, studentId);
        cacheEvictionHelper.clearCache(studentCourseResultsCache, studentId);

        cacheEvictionHelper.clearCacheMultipleEntries(groupStudentsCache, groupIds);

        if (studentCourseScoresCache != null) {
            for (Integer courseId : courseIds) {
                studentCourseScoresCache.evict(courseId + "_" + studentId);
            }
        }
    }
}
