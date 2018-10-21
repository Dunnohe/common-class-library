package common.rxjava2;

import io.reactivex.*;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.*;

/**
 * @author: linhu
 * @date: 2018/10/14 18:44
 * @description: 合并操作
 */
@Slf4j
public class _4CombiningOptTest {

    /**
     * and then (Completable中的方法最常用):在这个操作符中你可以传任何Observable、Single、Flowable、Maybe或者其他Completable，
     * 它们会在原来的 Completable 结束后执行
     */
    @Test
    public void testAndThen() {
        // Completable可以看成是runnable
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                log.info("hello world");

            }
        }).andThen((SingleSource<Object>) singleObserver -> log.info("Hi,how are you!")).subscribe();
    }

    /**
     * 当出现某个条件的时候就触发
     * 需要一个Observable 通过判断 throwableObservable,Observable发射一个数据 就重新订阅，发射的是 onError 通知，
     * 它就将这个通知传递给观察者然后终止。
     */
    @Test
    public void testRetryWhen() {
        Observable.just(1, "2", 3)
                .cast(Integer.class)
                .retryWhen(throwableObservable -> {
                    return throwableObservable.switchMap(throwable -> {
                        if (throwable instanceof IllegalArgumentException) {
                            return Observable.just(throwable);
                        }
                        return Observable.just(1).cast(String.class);
                    });
                })
                .subscribe(o -> {
                    log.info("test retry when value:{}", o);
                }, throwable -> {
                    log.error("test retry when error:", throwable);
                });
    }

    /**
     * 当两个 Observables 中的任何一个发射了数据时，使用一个函数结合每个 Observable 发射的最近数据项，
     * 并且基于这个函数的结果发射数据。
     */
    @Test
    public void testCombineLatest() {
        Observable<Integer> take_1 = just(1, 2, 3, 4, 5, 6).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 5;
            }
        }).take(1);

        Observable<Integer> take_2 = just(1, 2, 3, 4, 5, 6).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 5;
            }
        }).take(1);

        combineLatest(take_1, take_2, (integer, integer2) -> integer + integer2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                log.info("And Then When values:{}", integer);
            }
        });
    }

    /**
     * 任何时候，只要在另一个 Observable 发射的数据定义的时间窗口内，这个 Observable 发射了一条数据，
     * 就结合两个 Observable 发射的数据
     * join的效果类似于排列组合，把第一个数据源A作为基座窗口，他根据自己的节奏不断发射数据元素，第二个数据源B，每发射一个数据，
     * 我们都把它和第一个数据源A中已经发射的数据进行一对一匹配；举例来说，如果某一时刻B发射了一个数据“B”,
     * 此时A已经发射了0，1，2，3共四个数据，那么我们的合并操作就会把“B”依次与0,1,2,3配对，得到四组数据： 0, B 1, B 2, B 3, B
     */
    @Test
    public void testJoin() throws InterruptedException {
        Observable.intervalRange(10, 4, 0, 300, TimeUnit.MILLISECONDS)
                .join(Observable.interval(100, TimeUnit.MILLISECONDS)
                                .take(7)
                        , aLong -> {
                            log.info("开始收集：" + aLong);
                            return Observable.just(aLong);
                        }
                        , aLong -> Observable.timer(200, TimeUnit.MILLISECONDS)
                        , (aLong, aLong2) -> {
                            log.info("aLong:" + aLong + " aLong2:" + aLong2);
                            return aLong + aLong2;
                        }
                )
                .subscribe(aLong -> log.info("test join value:{}", aLong));
        Thread.sleep(10000);
    }

    /**
     * Merge 合并多个Observables的发射物
     * 根据时间线合并
     */
    @Test
    public void testMerge() throws InterruptedException {
        Observable<Long> ob1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribeOn(Schedulers.newThread());
        Observable<Long> ob2 = Observable.interval(50, TimeUnit.MILLISECONDS)
                .take(4)
                .map(aLong -> aLong + 10)
                .subscribeOn(Schedulers.newThread());

        merge(ob1, ob2)
                .subscribe(o -> log.info("test merge value:{}", o));

        Thread.sleep(5000);
    }

    /**
     * StartWith 在数据序列的开头插入一条指定的项
     * 是concat()的对应部分,在Observable开始发射他们的数据之前,startWith()通过传递一个参数来先发射一个数据序列
     */
    @Test
    public void testStartWith() {
        just(1)
                .startWith(2)
                .startWith(3)
                .startWith(just(4))
                .startWith(5)
                .startWithArray(6, 7)
                .subscribe(s -> log.info("test start with value:{}", s));
    }

    /**
     * Switch 将一个发射多个Observables的Observable转换成另一个单独的Observable，后者发射那些Observables最近发射的数据项
     * 如果原始Observable正常终止后仍然没有发射任何数据，就使用备用的Observable
     */
    @Test
    public void testSwitchIfEmpty() {
        Observable.empty()
                .switchIfEmpty(Observable.just(2, 3, 4))
                .subscribe(o -> log.info("test switchIfEmpty value:{}", o));

        Observable.just(1, 2, 3, 4, 5, 6).switchIfEmpty(Observable.just(7, 8, 9))
                .subscribe(o -> log.info("test switch value:{}", o));
    }

    /**
     * Zip 通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发射单个数据项。
     * 只有当原始的Observable中的每一个都发射了 一条数据时 zip 才发射数据。接受一到九个参数
     */
    @Test
    public void testZip() throws InterruptedException {
        Observable<Long> observable1 = interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .subscribeOn(Schedulers.newThread());

        Observable<Long> observable2 = interval(200, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribeOn(Schedulers.newThread());

        zip(observable1, observable2, (aLong, aLong2) -> {
            log.info("aLong:{},aLong2:{}", aLong, aLong2);
            return aLong + aLong2;
        }).subscribe(o -> log.info("test Zip value:{}", o));

        Thread.sleep(5000);
    }
}
