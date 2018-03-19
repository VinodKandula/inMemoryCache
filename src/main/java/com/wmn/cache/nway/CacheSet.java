package com.wmn.cache.nway;

import com.wmn.cache.CacheEntry;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class represents the cache set
 * @param <K>
 * @param <V>
 */
public class CacheSet<K,V> {

    //the concurrent read/write lock
    private final ReadWriteLock sLock = new ReentrantReadWriteLock();

    //atomic sequence that represents the 'age bit' of each onr of the entries
    private AtomicInteger ageBitGenerator;

    //the cache set lines
    private final List<CacheEntry<K,V>> lines;

    CacheSet(List<CacheEntry<K,V>> lines){
        this.lines = lines;
        ageBitGenerator = new AtomicInteger(0);
    }

    public ReadWriteLock getsLock() {
        return sLock;
    }

    public List<CacheEntry<K, V>> getLines() {
        return lines;
    }

    public int getLinesCapacity(){
        return getLines().size();
    }

    public int nextAgeBit(){
        return ageBitGenerator.incrementAndGet();
    }
}
