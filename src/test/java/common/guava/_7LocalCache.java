package common.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class _7LocalCache {

    private RemovalListener<Integer, Integer> listener = new RemovalListener<Integer, Integer>() {
        public void onRemoval(RemovalNotification<Integer, Integer> notification) {
            System.err.println("[remove] cache move success, key:{" + notification.getKey() + "}");
        }
    };

    LoadingCache<Integer, Integer> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(2)
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .removalListener(listener)
            .build(
                new CacheLoader<Integer, Integer>() {
                    public Integer load(Integer key) throws Exception {
                        // reload db
                        return -1;
                    }
                });

    /**
     * 测试超过最大数量被remove
     */
    @Test
    public void testCache() {
        loadingCache.put(1, 1);
        loadingCache.put(2, 2);
        loadingCache.put(3, 4);
    }

    /**
     * 测试超过最大数量被remove
     */
    @Test
    public void testCache2() throws InterruptedException, ExecutionException {
        loadingCache.put(1, 1);
        //查
        System.out.println(loadingCache.get(1));
        Thread.sleep(2000);//查
        System.out.println(loadingCache.get(1));
    }

}
