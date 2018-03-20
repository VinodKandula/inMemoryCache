package com.wmn.cache;

public interface Cache <K, V> {
    /**
     * get the value
     * @param key the key
     * @return the value
     */
    V get(K key);

    /**
     * put a new entry (key->value) in cache
     * @param key the key
     * @param value the value
     */
    void put(K key, V value);

    /**
     * removes the entry corresponding to the given key
     * @param key the key
     */
    void remove(K key);
}
