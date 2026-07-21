package com.example.argusclone.services.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

public abstract class AbstractCacheService {
    protected final CacheManager cacheManager;

    public AbstractCacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

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
