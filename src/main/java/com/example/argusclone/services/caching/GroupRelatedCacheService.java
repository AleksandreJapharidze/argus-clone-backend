package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupRelatedCacheService extends CacheEvictionHelper {
    private final CacheManager cacheManager;
    private final CacheEvictionHelper cacheEvictionHelper;

    public GroupRelatedCacheService(CacheManager cacheManager,
                                    CacheEvictionHelper cacheEvictionHelper) {
        this.cacheManager = cacheManager;
        this.cacheEvictionHelper = cacheEvictionHelper;
    }

    public void clearAllRelevantCachesForGroup(Integer groupId, Integer courseId, List<Integer> studentIds) {
        Cache groupCache = cacheManager.getCache("group-cache");
        Cache courseGroupsCache = cacheManager.getCache("course-groups-cache");
        Cache groupLecturesCache = cacheManager.getCache("group-lectures-cache");
        Cache groupStudentsCache = cacheManager.getCache("group-students-cache");
        Cache studentGroupIdsCache = cacheManager.getCache("student-group-ids-cache");
        Cache studentCoursesCache = cacheManager.getCache("student-courses-cache");
        Cache studentCourseScoresCache = cacheManager.getCache("student-course-scores-cache");
        Cache studentCourseIdsCache = cacheManager.getCache("student-course-ids-cache");

        cacheEvictionHelper.clearCache(groupCache, groupId);
        cacheEvictionHelper.clearCache(courseGroupsCache, courseId);
        cacheEvictionHelper.clearCache(groupLecturesCache, groupId);
        cacheEvictionHelper.clearCache(groupStudentsCache, groupId);

        cacheEvictionHelper.clearCacheMultipleEntries(studentGroupIdsCache, studentIds);
        cacheEvictionHelper.clearCacheMultipleEntries(studentCourseIdsCache, studentIds);
        cacheEvictionHelper.clearCacheMultipleEntries(studentCoursesCache, studentIds);

        if (studentCourseScoresCache != null) {
            for (Integer studentId : studentIds) {
                studentCourseScoresCache.evict(courseId + "_" + studentId);
            }
        }
    }
}
