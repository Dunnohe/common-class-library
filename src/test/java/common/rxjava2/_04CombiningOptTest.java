package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: linhu
 * @date: 2018/10/14 18:44
 * @description:
 */
@Slf4j
public class _04CombiningOptTest {

    /**
     * 使用Pattern和Plan作为中介，将两个或多个Observable发射的数据集合并到一起
     * 接受两个或多个Observable，一次一个将它们的发射物合并到Pattern对象，然后操作那个Pattern对象，
     * 变换为一个Plan。随后将这些Plan变换为Observable的发射物。
     */
    @Test
    public void testAnd_Then_When() {
    }

    /**
     * 当两个 Observables 中的任何一个发射了数据时，使用一个函数结合每个 Observable 发射的最近数据项，
     * 并且基于这个函数的结果发射数据。
     */
    @Test
    public void testCombineLatest() {
        Observable<Integer> take_1 = Observable.just(1, 2, 3, 4, 5, 6).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 5;
            }
        }).take(1);

        Observable<Integer> take_2 = Observable.just(1, 2, 3, 4, 5, 6).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer * 5;
            }
        }).take(1);

        Observable.combineLatest(take_1, take_2, (integer, integer2) -> integer + integer2).subscribe(new Consumer<Integer>() {
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
        Observable<Integer> observable_01 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter observableEmitter) throws Exception {
                for (int i = 1; i <= 5; i++) {
                    observableEmitter.onNext(i);

                }
            }
        });

        Observable<Integer> observable_02 = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter observableEmitter) throws Exception {
                Thread.sleep(1000);
                observableEmitter.onNext("B");
            }
        });
    }

    /**
     * Merge 合并多个Observables的发射物
     */
    @Test
    public void testMerge() throws InterruptedException {
        Observable<Long> ob1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .subscribeOn(Schedulers.newThread());
        Observable<Long> ob2 = Observable.interval(50, TimeUnit.MILLISECONDS)
                .take(3)
                .map(aLong -> aLong + 10)
                .subscribeOn(Schedulers.newThread());

        Observable.merge(ob1, ob2)
                .subscribe(o -> log.info("test merge value:{}", o));

        Thread.sleep(5000);
    }

    /**
     * StartWith 在数据序列的开头插入一条指定的项
     * 是concat()的对应部分,在Observable开始发射他们的数据之前,startWith()通过传递一个参数来先发射一个数据序列
     */
    @Test
    public void testStartWith() {
        Observable.just(1)
                .startWith(2)
                .startWith(3)
                .startWith(Observable.just(4))
                .startWith(5)
                .startWithArray(6, 7)
                .subscribe(s -> log.info("test start with value:{}", s));
    }

    /**
     * Switch 将一个发射多个Observables的Observable转换成另一个单独的Observable，后者发射那些Observables最近发射的数据项
     */
    @Test
    public void testSwitch() {

    }

    /**
     * Zip 通过一个函数将多个Observables的发射物结合到一起，基于这个函数的结果为每个结合体发射单个数据项。
     * 只有当原始的Observable中的每一个都发射了 一条数据时 zip 才发射数据。接受一到九个参数
     */
    @Test
    public void testZip() throws InterruptedException {
        Observable<Long> observable1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .take(3)
                .subscribeOn(Schedulers.newThread());

        Observable<Long> observable2 = Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(4)
                .subscribeOn(Schedulers.newThread());

        Observable.zip(observable1, observable2, (aLong, aLong2) -> {
            log.info("aLong:{},aLong2:{}", aLong, aLong2);
            return aLong + aLong2;
        }).subscribe(o -> log.info("test Zip value:{}", o));

        Thread.sleep(5000);
    }
}
