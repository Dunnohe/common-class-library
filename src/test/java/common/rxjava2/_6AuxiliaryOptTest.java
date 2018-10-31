package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 辅助操作
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
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 3; i++) {
                    log.info("before emit value:{}", i);
                    emitter.onNext(i);
                    log.info("after emit value:{}", i);

                }
            }
        }).doOnEach(integerNotification -> log.info("each callback value:{}", integerNotification.getValue()))
            .subscribe(new Observer<Integer>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Integer integer) {
                    log.info("consumer each value:{}", integer);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    /**
     * doOnNext 注类似doOnEach 不是接受一个 Notification 参数，而是接受发射的数据项
     */
    @Test
    public void testDoOnNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 3; i++) {
                    log.info("before emit value:{}", i);
                    emitter.onNext(i);
                    log.info("after emit value:{}", i);

                }
            }
        }).doOnNext(data -> log.info("each doOnNext value:{}", data))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        log.info("consumer each value:{}", integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * doOnComplete:注册一个动作，在观察者OnComplete时使用
     */
    @Test
    public void testDoOnComplete() {
        Observable.just(1, 2, 3, 4, 5, 6).doOnComplete(() -> {
            log.info("test do on complete");
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                log.info("consumer each value:{}", integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * materialize:将数据项和事件通知都当做数据项发射
     * dematerialize:和materialize相反
     */
    @Test
    public void testMaterializeAndDematerialize() {
        Observable.range(0, 3).materialize().subscribe(o -> {
            log.info("test materialize value:{}", o.getClass().getName());
        });
        /*Observable.range(0, 3).dematerialize().subscribe(o -> {
            log.info("test materialize value:{}", o);
        });*/
    }

    /**
     * ObserveOn:指定一个观察者在哪个调度器上观察这个Observable
     * 指定消费者
     */
    @Test
    public void testObserveOn() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

        Observable<Integer> integerObservable = Observable.just(1, 2, 3, 4, 5, 6);
        integerObservable.subscribe(o -> log.info("test ObserveOn value:{}", o));
        integerObservable.observeOn(Schedulers.from(executorService)).subscribe(o -> log.info("test ObserveOn value:{}", o));

        Thread.sleep(5000);
    }

    /**
     * subscribeOn:指定Observable自身在哪个调度器上执行
     */
    @Test
    public void testSubscribeOn() throws InterruptedException {
        ExecutorService produceExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("produce thread");
                return thread;
            }
        });

        ExecutorService consumerExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("consumer thread");
                return thread;
            }
        });

        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < 3; i++) {
                log.info("data1:{}", i);
                emitter.onNext(i);
            }
        }).observeOn(Schedulers.from(consumerExecutor)).subscribe(o -> log.info("data1 consumer:{}", o));

        log.info("=======");

        Observable<Integer> observable = Observable.create(emitter -> {
            for (int i = 0; i < 3; i++) {
                log.info("data2:{}", i);
                emitter.onNext(i);
            }
        });

        observable.subscribeOn(Schedulers.from(produceExecutor)).subscribe(o -> log.info("data2 consumer:{}", o));

        log.info("=======");
        Observable<Integer> observable1 = Observable.create(emitter -> {
            for (int i = 0; i < 3; i++) {
                log.info("data3:{}", i);
                emitter.onNext(i);
            }
        });
        observable1
                .subscribeOn(Schedulers.from(produceExecutor))
                .observeOn(Schedulers.from(consumerExecutor))
                .subscribe(o -> log.info("data3 consumer:{}", o));

        Thread.sleep(5000);
    }

    /**
     * Subscribe:操作来自Observable的发射物和通知
     * foreach:方法是简化版的 subscribe ，你同样可以传递一到三个函数给它，解释和传递给 subscribe 时是一样
     * 不同的是，你无法使用 forEach 返回的对象取消订阅。也没办法传递一个可以用于取消订阅 的参数
     */
    @Test
    public void testSubscribeAndForEach() {
        //subscribe的简化版本
        Observable.range(0, 3).forEach(o -> log.info("test for each value:{}", o));
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
                .forEach((Timed<Integer> o) -> log.info("test TimeInterval value:{}", o));
    }

    /**
     * TimeOut:过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，它会发一个错误
     */
    @Test
    public void testTimeout() throws InterruptedException {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                //过了一个指定的时长仍没有发射数据(不是仅仅考虑第一个)，它会发一个错误
                .timeout(50, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        log.info("time out---");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        log.info("============");

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
        log.info("data:{}", Observable.just(1, 2, 3, 4, 5, 6).toList().blockingGet());
        log.info("data:{}", Observable.just(7, 2, 3, 4, 5, 6).toSortedList().blockingGet());
        log.info("data:{}", Observable.just(7, 2, 3, 4, 5, 6).toMap(
                integer -> integer + "key",
                value -> value + "value",
                TreeMap::new).blockingGet());

    }

}
