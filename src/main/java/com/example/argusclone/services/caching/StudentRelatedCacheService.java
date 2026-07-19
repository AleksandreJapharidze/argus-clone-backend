package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentRelatedCacheService {
    private final CacheManager cacheManager;

    public StudentRelatedCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearAllRelevantCachesForStudent(Integer studentId, List<Integer> groupIds, List<Integer> courseIds) {
        Cache studentCoursesCache = cacheManager.getCache("student-courses-cache");
        Cache studentCourseIdsCache = cacheManager.getCache("student-course-ids-cache");
        Cache studentGroupIdsCache = cacheManager.getCache("student-group-ids-cache");
        Cache studentCourseScoresCache = cacheManager.getCache("student-course-scores-cache");
        Cache studentCourseResultsCache = cacheManager.getCache("student-course-results-cache");

        if (studentCoursesCache != null) {
            studentCoursesCache.evict(studentId);
        }

        if (studentCourseIdsCache != null) {
            studentCourseIdsCache.evict(studentId);
        }

        if (studentGroupIdsCache != null) {
            studentGroupIdsCache.evict(studentId);
        }

        if (studentCourseScoresCache != null) {
            for (Integer courseId : courseIds) {
                studentCourseScoresCache.evict(studentId + "_" + courseId);
            }
        }

        if (studentCourseResultsCache != null) {
            studentCourseResultsCache.evict(studentId);
        }
    }
}
