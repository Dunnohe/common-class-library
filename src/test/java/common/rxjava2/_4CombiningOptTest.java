package common.rxjava2;

import io.reactivex.*;
import io.reactivex.functions.Action;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @date: 2018/10/14 18:44
 * @description: 合并操作
 */
@Slf4j
public class _4CombiningOptTest {

    /**
     * 它们会在原来的 Completable 结束后执行
     * 顺序执行多个Completable
     */
    @Test
    public void testAndThen() {
        // Completable可以看成是runnable
        Completable hello_world = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                log.info("hello world");
            }
        });
        hello_world.andThen((SingleSource<Object>) singleObserver -> log.info("Hi,how are you!")).subscribe();
    }

    /**
     * 当出现某个条件的时候就触发
     * 需要一个Observable 通过判断 throwableObservable,Observable发射一个数据 就重新订阅，发射的是 onError 通知，
     * 它就将这个通知传递给观察者然后终止。
     * todo
     */
    @Test
    public void testRetryWhen() {
        Observable.just(1, "2", 3)
                .cast(Integer.class)
                .retryWhen(throwableObservable -> {
                    return throwableObservable.switchMap(throwable -> {
                        if (throwable instanceof ClassCastException) {
                            return Observable.just(4, 5, 6);
                        }
                        return Observable.empty();
                    });
                })
                .subscribe(o -> log.info("test retry when value:{}", o),
                        throwable -> log.error("test retry when error:", throwable));

    }

    /**
     * 当两个Observables中的任何一个发射了数据时，使用一个函数结合每个Observable发射的最近数据项，并且基于这个函数的结果发射数据。
     * 你的最新的和我的最新的合并
     * @See https://mcxiaoke.gitbooks.io/rxdocs/content/operators/CombineLatest.html
     */
    @Test
    public void testCombineLatest() throws InterruptedException {
        Observable<Integer> take_1 = Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS);

        take_1.subscribe(data -> log.info("data:{}", data));

        Observable<Integer> take_2 = Observable.just(4, 5, 6);

        take_2.subscribe(data -> log.info("data:{}", data));

        Observable.combineLatest(take_1, take_2, (integer, integer2) -> "" + integer + "-" + integer2)
                .subscribe(integer -> log.info("values:{}", integer));

        Thread.sleep(6000);
    }

    /**
     * 任何时候，只要在另一个 Observable 发射的数据定义的时间窗口内，这个 Observable 发射了一条数据，
     * 就结合两个 Observable 发射的数据
     * join的效果类似于排列组合，把第一个数据源A作为基座窗口，他根据自己的节奏不断发射数据元素，第二个数据源B，每发射一个数据，
     * 我们都把它和第一个数据源A中已经发射的数据进行一对一匹配；举例来说，如果某一时刻B发射了一个数据“B”,
     * 此时A已经发射了0，1，2，3共四个数据，那么我们的合并操作就会把“B”依次与0,1,2,3配对，得到四组数据： 0, B 1, B 2, B 3, B
     * @see "https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Join.html" todo 没必要研究
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
     * @see "https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Merge.html"
     */
    @Test
    public void testMerge() throws InterruptedException {
        Observable<Integer> take_1 = Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS);

        take_1.subscribe(data -> log.info("data:{}", data));

        Observable<Integer> take_2 = Observable.just(4, 5, 6);

        take_2.subscribe(data -> log.info("data:{}", data));

        Observable.merge(take_1, take_2).subscribe(integer -> log.info("values:{}", integer));

        Thread.sleep(6000);
    }

    /**s
     * StartWith 始终往最前面插入数据
     * 是concat()的对应部分,在Observable开始发射他们的数据之前,startWith()通过传递一个参数来先发射一个数据序列
     * @see "https://mcxiaoke.gitbooks.io/rxdocs/content/operators/StartWith.html"
     */
    @Test
    public void testStartWith() {
        Observable.just(3).startWith(1).startWith(2).subscribe(integer -> log.info("values:{}", integer));
        log.info("====");
        Observable.just(3).startWith(2).startWith(1).subscribe(integer -> log.info("values:{}", integer));
        log.info("====");
        Observable.just(3).startWith(2).startWithArray(-1, 0, 1).subscribe(integer -> log.info("values:{}", integer));
        log.info("====");
        Observable.just(3).startWith(2).startWith(Observable.just(0, -1)).subscribe(integer -> log.info("values:{}", integer));
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
     * 你的第一个和我的第一个合并，你的第二个和我的第二个合并，你的第N个和我的第N个合并
     * @see "https://mcxiaoke.gitbooks.io/rxdocs/content/operators/Zip.html"
     */
    @Test
    public void testZip() throws InterruptedException {
        Observable<Integer> take_1 = Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS);

        take_1.subscribe(data -> log.info("data:{}", data));

        Observable<Integer> take_2 = Observable.just(4, 5, 6);

        take_2.subscribe(data -> log.info("data:{}", data));

        Observable.zip(take_1, take_2, (integer1, integer2) -> integer1 + "-" + integer2)
                .subscribe(integer -> log.info("values:{}", integer));

        Thread.sleep(6000);
    }
}
