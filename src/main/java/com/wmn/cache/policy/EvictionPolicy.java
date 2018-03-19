package com.wmn.cache.policy;

import com.wmn.cache.CacheEntry;

import java.util.List;

/**
 * Functional interface to represent the caching eviction policy
 * @param <K>
 * @param <V>
 */

@FunctionalInterface
public interface EvictionPolicy <K,V>{

    /**
     * the eviction method
     * @param entries the set entries.
     */
    void evict(List<CacheEntry<K, V>> entries);

}
