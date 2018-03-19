package com.wmn.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Class represents th Cache Entry
 * @param <K>
 * @param <V>
 */
public class CacheEntry <K, V> {

    private final K key;

    private final V value;

    //when this entry was last touched (put/get)
    private AtomicLong touchTime;

    private final long creationTime;

    private int ageBit; //the age bit

    /**
     * Constructor
     * @param key the entry key
     * @param value the entry value
     */
    public CacheEntry(K key, V value){
        this.key = key;
        this.value = value;

        long t1 = System.currentTimeMillis();;
        this.creationTime = t1;
        this.touchTime = new AtomicLong(t1);

    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public AtomicLong getTouchTime() {
        return touchTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setTouchTime(long touchTime) {
        this.touchTime.set(touchTime);
    }

    public int getAgeBit() {
        return ageBit;
    }

    public void setAgeBit(int ageBit) {
        this.ageBit = ageBit;
    }

    public boolean equals(Object object){
        if (this == object) return true;

        if (object == null || !(object instanceof CacheEntry) ) return false;

        CacheEntry ce = (CacheEntry) object;;
        return key.hashCode() == ce.getKey().hashCode()
                && value.hashCode() == ce.getValue().hashCode();
    }

    public int hashCode(){
        return key.hashCode() + (5 * value.hashCode());
    }
}
