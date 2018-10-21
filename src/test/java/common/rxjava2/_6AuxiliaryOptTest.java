package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: linhu
 * @date: 2018/10/21 11:22
 * @description:辅助操作
 */
@Slf4j
public class _6AuxiliaryOptTest {

    /**
     * 延迟一段指定的时间再发射来自Observable的发射物  从Observable实例创建开始
     */
    @Test
    public void testDelay() throws InterruptedException {
        Observable<Integer> range = Observable.range(0, 3);
        log.info("test delay start");
        range.delay(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> log.info("test delay value:{}", o));
        Thread.sleep(2000);
    }

    /**
     * 注册一个动作作为原始Observable生命周期事件的一种占位符
     * doOnEach 注册一个回调，它产生的Observable每发射一项数据就会调用它一次 接受发射参数
     */
    @Test
    public void testDoOnEach() {
        Observable.range(0, 3)
                .doOnEach(integerNotification -> log.info("call back value:{}", integerNotification.getValue()))
                .subscribe(o -> log.info("test do on each value:{}", o));
    }

    /**
     * doOnNext 注类似doOnEach 不是接受一个 Notification 参数，而是接受发射的数据项
     */
    @Test
    public void testDoOnNext() {
        Observable.range(0, 3).doOnNext(integer -> {
            if (integer == 2) {
                throw new Error("value is error:2");
            }
            log.info("value is :{}", integer);
        }).subscribe(o -> log.info("test do on next"),
                throwable -> log.info("test do on next error:{}", throwable.getMessage()),
                () -> log.info("complete"));
    }

    /**
     * doOnComplete:注册一个动作，在观察者OnComplete时使用
     */
    @Test
    public void testDoOnComplete() {
        Observable.just(1, 2, 3, 4, 5, 6).doOnComplete(() -> {
            log.info("test do on complete");
        }).subscribe(o -> log.info("test do on complete value:{}", o));
    }

    /**
     * materialize:将数据项和事件通知都当做数据项发射
     * dematerialize:和materialize相反
     */
    @Test
    public void testMaterializeAndDematerialize() {
        Observable.range(0, 3).materialize().dematerialize().subscribe(o -> {
            log.info("test materialize value:{}", o);
        });
    }

    /**
     * ObserveOn:指定一个观察者在哪个调度器上观察这个Observable
     */
    @Test
    public void testObserveOn() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

        Observable<Integer> integerObservable = Observable.just(1, 2, 3, 4, 5, 6);
        integerObservable.observeOn(Schedulers.from(executorService)).subscribe(o -> log.info("test ObserveOn value:{}", o));
        Thread.sleep(5000);
    }

    /**
     * subscribeOn:指定Observable自身在哪个调度器上执行
     */
    @Test
    public void testSubscribeOn() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        Observable.just(1, 2, 3, 4, 5, 6).subscribeOn(Schedulers.from(executorService)).subscribe(o -> log.info("test subscribeOn value:{}", o));
        Thread.sleep(5000);
    }

    /**
     * 保证上游下游同一线程 ，防止不同线程下 数据不一致
     */
    @Test
    public void testSerialize() throws InterruptedException {
        Observable.range(0, 3)
                .serialize()
                .subscribe(o -> log.info("test Serialize value:{}", o));
    }

    /**
     * Subscribe:操作来自Observable的发射物和通知
     * foreach:方法是简化版的 subscribe ，你同样可以传递一到三个函数给它，解释和传递给 subscribe 时是一样
     * 不同的是，你无法使用 forEach 返回的对象取消订阅。也没办法传递一个可以用于取消订阅 的参数
     */
    @Test
    public void testSubscribeAndForEach() {
        Observable.range(0, 3)
                //subscribe的简化版本
                .forEach(o -> log.info("test for each value:{}", o));

        Observable.range(0, 3).subscribe(o -> log.info("test subscribe value:{}", o));
    }

    /**
     * Timestamp:它将一个发射T类型数据的Observable转换为一个发射类型 为Timestamped 的数据的Observable，
     * 每一项都包含数据的原始发射时间
     */
    @Test
    public void testTimestamp() throws InterruptedException {
        Observable.interval(100, TimeUnit.MILLISECONDS).
                timestamp().
                take(3).
                forEach(o -> log.info("test timestamp value:{}", o));
        Thread.sleep(1000);
    }

    /**
     * timeInterval:一个发射数据的Observable转换为发射那些数据发射时间间隔的Observable
     */
    @Test
    public void testTimeInterval() {
        Observable.just(1, 2, 3, 4, 5, 6)
                // 把发送的数据 转化为  相邻发送数据的时间间隔实体
                .timeInterval()
                .forEach(o -> log.info("test TimeInterval value:{}", o));
    }

    /**
     * TimeOut:过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，它会发一个错误
     */
    @Test
    public void testTimeout() throws InterruptedException {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                //过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，它会发一个错误
                .timeout(50, TimeUnit.MILLISECONDS)
                .subscribe(o -> log.info("test time out value:{}", o)
                        , error -> log.info("time out error:{}", error.getMessage())
                        , () -> log.info("test timeout complete")
                        , disposable -> log.info("test timeout 订阅"));

        Observable<Integer> other;
        Observable.empty()
                // 过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，他会用备用Observable 发送数据，本身的会发送一个compelte
                .timeout(50, TimeUnit.MILLISECONDS, other = Observable.just(2, 3, 4))
                .subscribe(o -> log.info("test time out 2 value:{}", o)
                        , error -> log.info("time out 2 error:{}", error.getMessage())
                        , () -> log.info("test timeout 2 complete")
                        , disposable -> log.info("test timeout 2 订阅"));
        other.subscribe(o -> log.info("test other time out 2 value:{}", o));


        Thread.sleep(1000);
    }

    /**
     * To:将Observable转换为另一个对象或数据结构
     */
    @Test
    public void testTo() {
        Observable.just(1, 2, 3, 4, 5, 6).toList().blockingGet().forEach(o -> log.info("test to list value:{}", o));

        Observable.just(5, 1, 7, 2, 9, 3).toSortedList().blockingGet().forEach(o -> log.info("test to sorted list value:{}", o));

        Map<String, Integer> toMap = Observable.just(1, 2, 3).toMap(integer -> integer + "key", Integer::intValue, () -> new HashMap<>()).blockingGet();
        log.info("test to Map :{}",toMap);

    }

}
