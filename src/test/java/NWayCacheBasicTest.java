import com.wmn.cache.nway.NWayAssociativeCache;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NWayCacheBasicTest {

    private final int NUM_SETS = 2;

    private final int NUM_LINES = 2;

    private NWayAssociativeCache<String, Integer> basicNWayCache;

    @Before
    public void setUp(){
        basicNWayCache = new NWayAssociativeCache.Builder<String, Integer>(NUM_SETS,NUM_LINES)
                .build();
    }


    @Test(expected = IllegalArgumentException.class)
    public void checkBuilderNumOfSetsValidation(){
        new NWayAssociativeCache.Builder<String, Integer>(0,NUM_LINES)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBuilderNumOfLinesValidation(){
        new NWayAssociativeCache.Builder<String, Integer>(NUM_SETS, 0)
                .build();
    }


    @Test(expected = IllegalArgumentException.class)
    public void checkCacheKeyInGetValidation(){
        basicNWayCache.get(null);
    }





    @Test
    public void testBasicPutAndGet(){
        final String key1 = "number";
        basicNWayCache.put(key1, 1);

        Integer i = basicNWayCache.get(key1);

        Assert.assertNotNull(i);

        Assert.assertTrue(i == 1);
    }

    @Test
    public void testMultipleKeysPutAndGet(){
        final String key1 = "number";
        final String key2 = "number2";
        basicNWayCache.put(key1, 1);
        basicNWayCache.put(key2, 2);

        Integer i1 = basicNWayCache.get(key1);
        Integer i2 = basicNWayCache.get(key2);

        Assert.assertNotNull(i1);
        Assert.assertNotNull(i2);

        Assert.assertTrue(i1 == 1);
        Assert.assertTrue(i2 == 2);
    }

    @Test
    public void testPutGetAndRemove(){
        final String key1 = "number";

        basicNWayCache.put(key1, 1);

        Integer i1 = basicNWayCache.get(key1);

        Assert.assertNotNull(i1);
        Assert.assertTrue(i1 == 1);

        basicNWayCache.remove(key1);

        i1 = basicNWayCache.get(key1);
        Assert.assertNull(i1);
    }


    @Test
    public void testMultiplePuts(){

        NWayAssociativeCache<String, Integer> cache = new NWayAssociativeCache.Builder<String, Integer>(1,2)
                .build();

        cache.put("1", 1);
        cache.put("2", 2);
        cache.put("3", 3);

        Assert.assertNotNull(cache.get("3"));
        Assert.assertNotNull(cache.get("2"));

        //check eviction work..
        Assert.assertNull(cache.get("1"));
    }

    @Test
    public void testMultiplePutsAndGets(){

        NWayAssociativeCache<String, Integer> cache = new NWayAssociativeCache.Builder<String, Integer>(1,2)
                .build();

        cache.put("1", 1);
        cache.put("2", 2);
        cache.get("1");
        cache.put("3", 3);
        cache.get("1");

        Assert.assertNotNull(cache.get("3"));
        Assert.assertNotNull(cache.get("1"));

        //check eviction work..
        Assert.assertNull(cache.get("2"));
    }



}
