package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author: linhu
 * @date: 2018/10/14 14:15
 * @description:
 */
@Slf4j
public class _2TransformingOptTest {

    /**
     * buffer操作符：定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
     */
    @Test
    public void testBuffer() {
        // 1.buffer(count)
        // 以列表(List)的形式发射非重叠的缓存，每一个缓存至多包含来自原始Observable的count项数据（最后发射的列表数据可能少于count项）
        Observable.just(1, 2, 3, 4, 5, 6, 7).
                buffer(3).
                subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        log.info("test buffer(count) size:{},values:{}", integers.size(), integers);
                    }
                });


        // 2.buffer(count,skip)
        // 作用是将 Observable 中的数据按 skip (步长) 分成最大不超过 count 的 buffer ，然后生成一个  Observable
        Observable.just(1, 2, 3, 4, 5, 6).
                buffer(3, 2).
                subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        log.info("test buffer(count,skip) size:{},values:{}", integers.size(), integers);
                    }
                });
    }


    /**
     * FlatMap将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据处理合并后放进一个单独的Observable
     * 但有个需要注意的是，flatMap 并不能保证事件的顺序，如果需要保证，需要用到ConcatMap。它与FlatMap唯一区别就是保证处理后的顺序
     */
    @Test
    public void testFlatMap() {
        Observable.just(1, 2, 3, 4, 5, 6).
                flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {

                        String result = "I am " + integer;
                        return Observable.fromIterable(Arrays.asList(result));
                    }
                }).
                subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        log.info("test flatMap : value : {}", s);
                    }
                });
    }

    /**
     * Map  对Observable发射的每一项数据应用一个函数，执行变换操作
     */
    @Test
    public void testMap() {
        Observable.just(1, 2, 3, 4, 5, 6).
                map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer + 1;
                    }
                }).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log.info("test Map value:{}", integer);
                    }
                });
    }

    /**
     * GroupBy 将一个Observable分拆为一些Observables集合，它们中的每一个发射原始Observable的一个子序列
     */
    @Test
    public void testGroupBy() {
        Observable.just(1, 2, 3, 4, 5, 6).
                groupBy(integer -> integer % 2, integer -> "(" + integer + ")").
                subscribe(group -> group.subscribe(signGroup -> log.info("test group by key:{},values:{}", group.getKey(), signGroup)));
    }

    /**
     * Scan连续地对数据序列的每一项应用一个函数，然后对每一项连续发射输出结果
     */
    @Test
    public void testScan() {
        Observable.just(1, 2, 3, 4, 5, 6).
                scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log.info("test scan value:{}", integer);
                    }
                });
    }

    /**
     * Reduce连续对数据序列每一项应用一个函数，但是区别于Scan，只是输出最后处理结果
     */
    @Test
    public void testReduce() {
        Observable.just(1, 2, 3, 4, 5, 6).
                reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log.info("test reduce value:{}", integer);
                    }
                });
    }

    /**
     * Window 定期将来自原始Observable的数据分解为一个Observable窗口，发射这些窗口，而不是每次发射一项数据
     * 与buffer一样，区别是Window取自Observables对象，但是buffer取得是数据包
     */
    @Test
    public void testWindow() {
        Observable.just(1, 2, 3, 4, 5, 6).
                window(3)
                .subscribe(integerObservable -> {
                    integerObservable.subscribe(integer -> log.info(integerObservable + "===>" + integer));
                });
    }

}
