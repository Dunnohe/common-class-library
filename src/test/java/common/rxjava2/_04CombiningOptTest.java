package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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
        }).take(2);

        Observable.combineLatest(take_1, take_2, (integer, integer2) -> integer + integer2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                log.info("And Then When values:{}", integer);
            }
        });


    }
}
