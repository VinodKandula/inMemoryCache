package com.wmn.cache.nway;

import com.wmn.cache.AbstractEvictionCache;
import com.wmn.cache.CacheEntry;
import com.wmn.cache.outlet.CacheOutlet;
import com.wmn.cache.policy.EvictionPolicy;
import com.wmn.cache.policy.LRUEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class implementing the N Way Associative Cache
 * @param <K>
 * @param <V>
 */
public class NWayAssociativeCache<K,V> extends AbstractEvictionCache<K,V>{

    private final Logger logger = LoggerFactory.getLogger(NWayAssociativeCache.class.getName());

    //holds immutable cache sets
    private List<CacheSet<K,V>> immutableCacheSets;

    /**
     * The builder class that will help create a NWayAssociativeCache instance
     * @param <K>
     * @param <V>
     */
    public static class Builder<K,V>{
        private int numSets;
        private int numLines;

        private EvictionPolicy<K,V>  evictionPolicy;
        private CacheOutlet<K,V> cacheOutlet;

        public Builder(int numSets, int numLines){
            if (numSets < 1 || numLines < 1)
                throw new CacheBuilderException("numOfSets and numOfLines must greater or equals 1");

            this.numSets = numSets;
            this.numLines = numLines;
            this.evictionPolicy = new LRUEvictionPolicy<>();
        }

        public Builder<K,V> withEvictionPolicy(EvictionPolicy<K,V> evictionPolicy){
            if (evictionPolicy == null)
                throw new CacheBuilderException("Eviction Policy cannot be null");
            this.evictionPolicy = evictionPolicy;
            return this;
        }

        public Builder<K,V> withCacheOutlet(CacheOutlet<K,V> cacheOutlet){
            this.cacheOutlet = cacheOutlet;
            return this;
        }

        /**
         * The cache init method
         * @return a new NWayAssociativeCache instance
         */
        public NWayAssociativeCache<K,V> build(){
            NWayAssociativeCache<K,V> nWayAssoCache = new NWayAssociativeCache<>(numSets, numLines);
            nWayAssoCache.setEvictionPolicy(evictionPolicy);
            if (cacheOutlet != null){
                nWayAssoCache.setCacheOutlet(cacheOutlet);
            }

            return nWayAssoCache;
        }
    }

    private int numSets;
    private int numLines;

    private NWayAssociativeCache(int numSets, int numLines){
        this.numSets = numSets;
        this.numLines = numLines;

        List<CacheSet<K, V>> cacheSets = new ArrayList<>(this.numSets);

        IntStream.range(0, numSets).forEach(i->
                cacheSets.add(i, new CacheSet<>(new LinkedList<>())));

        immutableCacheSets = Collections.unmodifiableList(cacheSets);

    }

    @Override
    public V get(K key)  {
        if (key == null)
            throw new IllegalArgumentException("key can not be null");

        int sIndex = NWayCacheUtil.findSetIndex(key, numSets);
        CacheSet<K,V> set = immutableCacheSets.get(sIndex);

        CacheEntry<K,V> entry = getCacheEntry(set, key);

        if (entry == null){ //no entry was found in cache
            if (logger.isDebugEnabled()) {
                logger.debug("No value was found for key : " + key + " (" + key.hashCode() + ")");
            }

            //if cache outlet configured
            if (getCacheOutlet() != null){
                if (logger.isDebugEnabled()) {
                    logger.debug("using cache outlet for retrieving value");
                }
                V value = getCacheOutlet().obtain(key);
                put(key, value);
                return  value;
            }
            return null;
        }

        updateEntryAccess(entry, set.nextAgeBit());

        return entry.getValue();
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException("key and value must not be null");

        if(getEvictionPolicy() == null)
            throw new NullPointerException("Eviction Policy is not configured ");

        int sIndex = NWayCacheUtil.findSetIndex(key, numSets);
        CacheSet<K, V> set = immutableCacheSets.get(sIndex);

        try {
            set.getsLock().writeLock().lock();
            //in case there is no more space for moew line in set - need to call for eviction
            if (set.getLinesCapacity() >= numLines) {
                //perform eviction
                getEvictionPolicy().evict(set.getLines());
            }

            //at this point we have more space to add the new entry
            CacheEntry<K, V> entry = new CacheEntry<>(key, value);
            entry.setAgeBit(set.nextAgeBit()); //providing age bit

            set.getLines().add(entry);

            if (logger.isDebugEnabled()) {
                logger.debug("entry '" + entry.getKey() + "' was entered");
            }

        } finally {
            set.getsLock().writeLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        if (key == null )
            throw new IllegalArgumentException("key must not be null");

        int sIndex = NWayCacheUtil.findSetIndex(key, numSets);
        CacheSet<K,V> set = immutableCacheSets.get(sIndex);

        CacheEntry<K,V> entry = getCacheEntry(set, key);

        try{
            set.getsLock().writeLock().lock();

            set.getLines().removeIf(e -> e.equals(entry));

        }finally {
            set.getsLock().writeLock().unlock();
        }
    }

    /**
     * Updates cache entry access data
     * @param entry the entry
     * @param ageBit the new age bit
     */
    private void updateEntryAccess(CacheEntry<K,V> entry, int ageBit){
        //set touch time for entry
        entry.setTouchTime(System.currentTimeMillis());
        //update age bit
        entry.setAgeBit(ageBit);
    }

    /**
     * get stored value from cache
     * @param key by the key
     * @return the value , null if missing
     */
    private CacheEntry<K,V> getCacheEntry( CacheSet<K,V> set, K key){
        try {
            set.getsLock().readLock().lock();

            List<CacheEntry<K, V>> filter = set.getLines()
                    .stream()
                    .filter(l -> l.getKey().equals(key))
                    .collect(Collectors.toList());

            if (filter == null || filter.isEmpty()) return null;

            return filter.get(0);
        }finally {
            set.getsLock().readLock().unlock();
        }

    }
}
