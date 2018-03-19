package com.wmn.cache;

import com.wmn.cache.outlet.CacheOutlet;
import com.wmn.cache.policy.EvictionPolicy;

/**
 * An abstract cache representation
 * @param <K>
 * @param <V>
 */
public abstract class AbstractEvictionCache <K,V> implements Cache <K, V>{

    //holds the eviction policy algorithm
    private EvictionPolicy<K,V> evictionPolicy;

    //the cache outlet (for getting value outside from the cache)
    private CacheOutlet<K, V> cacheOutlet;


    public EvictionPolicy<K,V> getEvictionPolicy(){
        return this.evictionPolicy;
    }

    protected void setEvictionPolicy(EvictionPolicy<K,V> evictionPolicy){
        this.evictionPolicy = evictionPolicy;
    }

    public CacheOutlet<K, V> getCacheOutlet() {
        return cacheOutlet;
    }

    public void setCacheOutlet(CacheOutlet<K, V> cacheOutlet) {
        this.cacheOutlet = cacheOutlet;
    }
}
