
import com.wmn.cache.nway.CacheBuilderException;
import com.wmn.cache.nway.NWayAssociativeCache;
import com.wmn.cache.policy.LRUEvictionPolicy;
import com.wmn.cache.policy.MRUEvictionPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CacheBasicBuilderFeatureTest {

    private NWayAssociativeCache<String, Integer> basicNWayCache;

    @Before
    public void setUp(){
        basicNWayCache = new NWayAssociativeCache.Builder<String, Integer>(2,2)
                .build();
    }

    @Test(expected = CacheBuilderException.class)
    public void testIllegalNumOfSetValue(){
        new NWayAssociativeCache.Builder<String, Integer>(0,2)
                .build();
    }

    @Test(expected = CacheBuilderException.class)
    public void testIllegalNumOfLinesValue(){
        new NWayAssociativeCache.Builder<String, Integer>(2,0)
                .build();
    }

    @Test
    public void testNWayCacheBuilder(){
        Assert.assertTrue(basicNWayCache != null);
    }

    @Test
    public void testDefaultEvictionPolicyIsLRU(){
        Assert.assertTrue(basicNWayCache.getEvictionPolicy() instanceof LRUEvictionPolicy);
    }

    @Test
    public void testDefaultCacheOutletIsEmpty(){
        Assert.assertNull(basicNWayCache.getCacheOutlet());
    }

    @Test
    public void testChangeCacheOutlet(){
        NWayAssociativeCache<String, Integer> cacheWithNewEviction = new NWayAssociativeCache.Builder<String, Integer>(2,2)
                .withEvictionPolicy(new MRUEvictionPolicy<>())
                .withCacheOutlet(lst-> 1)
                .build();

        Assert.assertNotNull(cacheWithNewEviction.getCacheOutlet());
    }

    @Test
    public void testChangeEvictionPolicyInBuilder(){
        NWayAssociativeCache<String, Integer> cacheWithNewEviction = new NWayAssociativeCache.Builder<String, Integer>(2,2)
                .withEvictionPolicy(new MRUEvictionPolicy<>())
                .build();

        Assert.assertNotNull(cacheWithNewEviction.getEvictionPolicy());
        Assert.assertTrue(cacheWithNewEviction.getEvictionPolicy() instanceof MRUEvictionPolicy);
    }

    @Test(expected = CacheBuilderException.class)
    public void testChangeEvictionPolicyInBuilderToNull(){
        new NWayAssociativeCache.Builder<String, Integer>(2,2)
                .withEvictionPolicy(null)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey(){
        basicNWayCache.put(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue(){
        basicNWayCache.put("1", null);
    }

}
