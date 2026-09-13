package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CacheEvictionHelper {
    protected void clearCache(Cache cache, Integer id) {
        if (cache != null) {
            cache.evict(id);
        }
    }

    protected void clearCacheMultipleEntries(Cache cache, List<Integer> ids) {
        if (cache != null) {
            for (Integer id : ids) {
                cache.evict(id);
            }
        }
    }
}
