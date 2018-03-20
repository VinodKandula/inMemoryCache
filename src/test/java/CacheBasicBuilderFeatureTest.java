
import com.wmn.cache.nway.NWayAssociativeCache;
import com.wmn.cache.policy.LRUEvictionPolicy;
import com.wmn.cache.policy.MRUEvictionPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CacheBasicBuilderFeatureTest {

    NWayAssociativeCache<String, Integer> basicNWayCache;

    @Before
    public void setUp(){
        basicNWayCache = new NWayAssociativeCache.Builder<String, Integer>(2,2)
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
    public void testChangeEvictionPolicyInBuilder(){
        NWayAssociativeCache<String, Integer> cacheWithNewEviction = new NWayAssociativeCache.Builder<String, Integer>(2,2)
                .withEvictionPolicy(new MRUEvictionPolicy<>())
                .build();

        Assert.assertNotNull(cacheWithNewEviction.getEvictionPolicy());
        Assert.assertTrue(cacheWithNewEviction.getEvictionPolicy() instanceof MRUEvictionPolicy);
    }
}
