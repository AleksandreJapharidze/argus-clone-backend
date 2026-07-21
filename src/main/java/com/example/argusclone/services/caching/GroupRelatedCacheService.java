package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupRelatedCacheService extends AbstractCacheService {
    public GroupRelatedCacheService(CacheManager cacheManager) {
        super(cacheManager);
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

        clearCache(groupCache, groupId);
        clearCache(courseGroupsCache, courseId);
        clearCache(groupLecturesCache, groupId);
        clearCache(groupStudentsCache, groupId);

        clearCacheMultipleEntries(studentGroupIdsCache, studentIds);
        clearCacheMultipleEntries(studentCourseIdsCache, studentIds);
        clearCacheMultipleEntries(studentCoursesCache, studentIds);

        if (studentCourseScoresCache != null) {
            for (Integer studentId : studentIds) {
                studentCourseScoresCache.evict(courseId + "_" + studentId);
            }
        }
    }
}
