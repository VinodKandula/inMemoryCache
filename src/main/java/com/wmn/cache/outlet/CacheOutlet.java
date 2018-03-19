package com.wmn.cache.outlet;

/**
 * Interface reresents the cache outlet
 * @param <K>
 * @param <V>
 */
@FunctionalInterface
public interface CacheOutlet<K,V> {

    /**
     * this methos is expected to direct the call to a third party data source for obtaining a value for the given key
     * @param key the key
     * @return
     */
    V obtain(K key);

}
