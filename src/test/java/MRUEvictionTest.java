import com.wmn.cache.CacheEntry;
import com.wmn.cache.policy.EvictionPolicyException;
import com.wmn.cache.policy.MRUEvictionPolicy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class MRUEvictionTest {

    private MRUEvictionPolicy<String, Integer> mruEviction;

    private List<CacheEntry<String, Integer>> entries;

    @Before
    public void setup(){
        mruEviction = new MRUEvictionPolicy<>();
        entries = new LinkedList<>();
    }

    @Test(expected = EvictionPolicyException.class)
    public void testEmptyEntries(){
        mruEviction.evict(new LinkedList<>());
    }

    @Test(expected = EvictionPolicyException.class)
    public void testNullEntries(){
        mruEviction.evict(null);
    }

    @Test
    public void testEvictionRemoveOne(){
        createEntries();
        int old = entries.size();
        mruEviction.evict(entries);
        Assert.assertEquals(old-1, entries.size());
    }

    private void createEntries(){
        IntStream.range(0,5).forEach(i->{
            CacheEntry<String, Integer> entry = new CacheEntry<>(String.valueOf(i), i);
            entries.add(entry);
        });
    }



}
