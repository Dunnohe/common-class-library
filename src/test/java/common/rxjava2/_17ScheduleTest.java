package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 调度器的使用
 */
@Slf4j
public class _17ScheduleTest {

    /**
     * 用于计算任务，如事件循环或和回调处理，默认线程数等于处理器的数量
     * 不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
     */
    @Test
    public void testComputation() throws InterruptedException {
        Observable.just(1, 2, 3).observeOn(Schedulers.computation()).subscribe(data -> log.info("data:{}", data));
        Thread.sleep(5000);
    }

    /**
     * 用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；对于普通的计算任务，
     * 请使用Schedulers.computation()；Schedulers.io( )默认是一个CachedThreadScheduler，很像一个有线程缓存的新线程调度器
     */
    @Test
    public void testIo() throws InterruptedException {
        Observable.just(1, 2, 3).observeOn(Schedulers.io()).subscribe(data -> log.info("data:{}", data));
        Thread.sleep(5000);
    }

    /**
     * 使用指定的Executor作为调度器
     */
    @Test
    public void testFrom() throws InterruptedException {
        ExecutorService consumerExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("produce thread");
                return thread;
            }
        });

        Observable.just(1, 2, 3).observeOn(Schedulers.from(consumerExecutor)).subscribe(data -> log.info("data:{}", data));
        Thread.sleep(5000);
    }

    /**
     * 为每个任务创建一个新线程
     */
    @Test
    public void testNewThread() throws InterruptedException {
        Observable.just(1, 2, 3).observeOn(Schedulers.newThread()).subscribe(data -> log.info("data:{}", data));
        Thread.sleep(5000);
    }

    /**
     * 当其它排队的任务完成后，在当前线程排队开始执行
     */
    @Test
    public void testTrampoline() throws InterruptedException {

        Observable.just(1, 2, 3).observeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline()).subscribe(data -> log.info("data:{}", data));
        Observable.just(1, 2, 3).observeOn(Schedulers.trampoline()).subscribe(data -> log.info("data:{}", data));
        Thread.sleep(5000);
    }
}
