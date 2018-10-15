package common.rxjava2;

import com.sun.tools.corba.se.idl.StringGen;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
}
