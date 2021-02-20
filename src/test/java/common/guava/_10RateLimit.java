package common.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

public class _10RateLimit {

    //速率是每秒两个许可
    final RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
            System.out.println("task exec, number:" + i);
        }
    }
}
