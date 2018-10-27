package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * 阻塞操作
 */
@Slf4j
public class _9BlockOptTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * 如果Observable终止时只发射了一个值，返回那个值，否则抛出异常
     */
    @Test
    public void testSingle() {
        //获得当个值
        Integer value = Observable.just(1).single(-1).blockingGet();
        log.info("data:{}", value);

        Observable.just(1, 2, 3).singleOrError().blockingGet();
        thrown.expect(IllegalArgumentException.class);

    }

    /**
     * 其实就是for循环，挨个操作
     */
    @Test
    public void testForeach() {
        Observable.just(1, 2, 3).forEach(integer -> {
            log.info("data:{}", integer);
        });
    }

    /**
     * 返回一个总是返回Observable最近发射的数据的iterable
     */
    @Test
    public void testMostRecent() {
        Observable.just(1, 2, 3)
                .blockingMostRecent(1).forEach(value -> log.info("data:{}", value));
    }

    /**
     * 转换成collection
     */
    @Test
    public void testToCollection() {
        Observable.just(1, 2, 3).
                toList().blockingGet().iterator().forEachRemaining(value -> log.info("data:{}", value));

    }
}
