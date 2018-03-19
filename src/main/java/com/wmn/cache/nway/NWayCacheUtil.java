package com.wmn.cache.nway;

/**
 * The cache helper util
 */
public class NWayCacheUtil {


    /**
     * calculate the correct set index
     * @param key the key
     * @param numberOfSets total number of sets
     * @return the set index
     */
    public static int findSetIndex(Object key, int numberOfSets){
        int hashcode =  Math.abs(key.hashCode());
        return hashcode % numberOfSets;
    }
}
