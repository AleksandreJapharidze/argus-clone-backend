package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupRelatedCacheService {
    private final CacheManager cacheManager;

    public GroupRelatedCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearAllRelevantCachesForGroup(Integer groupId, Integer courseId, List<Integer> studentIds) {
        Cache groupCache = cacheManager.getCache("group-cache");
        Cache courseGroupsCache = cacheManager.getCache("course-groups-cache");
        Cache groupLecturesCache = cacheManager.getCache("group-lectures-cache");
        Cache studentGroupIdsCache = cacheManager.getCache("student-group-ids-cache");

        if (groupCache != null) {
            groupCache.evict(groupId);
        }

        if (courseGroupsCache != null) {
            courseGroupsCache.evict(courseId);
        }

        if (groupLecturesCache != null) {
            groupLecturesCache.evict(groupId);
        }

        if (studentGroupIdsCache != null) {
            for (Integer studentId : studentIds) {
                studentGroupIdsCache.evict(studentId);
            }
        }
    }
}
