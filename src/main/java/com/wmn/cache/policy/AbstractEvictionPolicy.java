package com.wmn.cache.policy;

import com.wmn.cache.CacheEntry;

import java.util.List;

public class AbstractEvictionPolicy<K,V> implements EvictionPolicy<K,V>{

    @Override
    public void evict(List<CacheEntry<K, V>> cacheEntries) {
        if (cacheEntries == null || cacheEntries.isEmpty())
            throw new EvictionPolicyException("cache entries are null or empty");
    }
}
