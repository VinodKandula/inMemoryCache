import com.wmn.cache.CacheEntry;
import com.wmn.cache.policy.EvictionPolicyException;
import com.wmn.cache.policy.LRUEvictionPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class LRUEvictionTest {

    private LRUEvictionPolicy<String, Integer> lruEviction;

    private List<CacheEntry<String, Integer>> entries;

    @Before
    public void setup(){
        lruEviction = new LRUEvictionPolicy<>();
        entries = new LinkedList<>();
    }

    @Test(expected = EvictionPolicyException.class)
    public void testEmptyEntries(){
        lruEviction.evict(new LinkedList<>());
    }

    @Test(expected = EvictionPolicyException.class)
    public void testNullEntries(){
        lruEviction.evict(null);
    }

    @Test
    public void testEvictionRemoveOne(){
        createEntries();
        int old = entries.size();
        lruEviction.evict(entries);
        Assert.assertEquals(old-1, entries.size());
    }




    private void createEntries(){
        IntStream.range(0,5).forEach(i->{
            CacheEntry<String, Integer> entry = new CacheEntry<>(String.valueOf(i), i);
            entries.add(entry);
        });
    }



}
