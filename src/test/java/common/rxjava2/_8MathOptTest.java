package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.OptionalDouble;

import static org.junit.Assert.assertTrue;

/**
 * 使用这个测试时，需要额外引入rxjava-math
 */
@Slf4j
public class _8MathOptTest {

    /**
     * 算平均值
     */
    @Test
    public void testAverage() {
        OptionalDouble average = Observable.just(1, 2, 3, 4).toList().blockingGet().stream().mapToInt(Integer::intValue).average();
        log.info("data:{}", average.isPresent() ? average.getAsDouble() : 0);
    }

    /**
     * 判定一个Observable是否发射一个特定的值
     */
    @Test
    public void testConcat() {
        assertTrue(Observable.just(1, 2, 3, 4).contains(2).blockingGet());
    }

    @Test
    public void testCount() {
        long count = (long) Observable.just(1, 2, 3, 4).toList().blockingGet().size();
        log.info("data:{}", count);

    }

    /**
     * 用java8替代
     */
    @Test
    public void testMax() {
        Integer integer = Observable.just(1, 2, 3, 4).toList().blockingGet().stream().max(Integer::compareTo).get();
        log.info("data:{}", integer);
    }

    @Test
    public void testMin() {
        Integer integer = Observable.just(1, 2, 3, 4).toList().blockingGet().stream().min(Integer::compareTo).get();
        log.info("data:{}", integer);

    }

    /**
     * 上一次处理的结果作为下一次的入参
     */
    @Test
    public void testReduce() {
        String s = Observable.just(1, 2, 3).reduce("", new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String s, Integer integer) throws Exception {
                return s + integer;
            }
        }).blockingGet();

        log.info("data:{}", s);
    }

    @Test
    public void testSum() {
        int sum = Observable.just(1, 2, 3, 4).toList().blockingGet().stream().mapToInt(Integer::intValue).sum();
        System.out.println();
        log.info("data:{}", sum);

    }

}
