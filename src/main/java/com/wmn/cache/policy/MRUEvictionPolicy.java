package com.wmn.cache.policy;

import com.wmn.cache.CacheEntry;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implemenation of the MRU algorithm
 * @param <K>
 * @param <V>
 */
public class MRUEvictionPolicy<K,V> extends AbstractEvictionPolicy<K,V> {

    @Override
    public void evict(List<CacheEntry<K, V>> cacheEntries) {
        super.evict(cacheEntries);

        //getting the entry with the smallest ageBit (means the least recently used)
        CacheEntry<K, V> max = cacheEntries.stream()
                .max(Comparator.comparingInt(CacheEntry::getAgeBit))
                .orElseThrow(NoSuchElementException::new);

        cacheEntries.removeIf(e -> e.equals(max));
    }
}
