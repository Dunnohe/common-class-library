package common.rxjava2;

import com.google.common.base.Optional;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

@Slf4j
public class _7ConditionAndBoolOptTest {

    /**
     * 判定是否Observable发射的所有数据都满足某个条件
     */
    @Test
    public void testAll() {
        //When we use toBlocking then we get result immediately. When we use subscribe then result is obtained asynchronously.
        boolean b = Observable.just(1, 2, 3, 4, 5).all(integer -> integer > 0).blockingGet();
        assertTrue(b);
    }

    /**
     * 判定一个Observable是否发射一个特定的值
     */
    @Test
    public void testContains() {
        assertTrue(Observable.just(1, 2, 3, 4).contains(2).blockingGet());
    }

    /**
     * 给定多个Observables，它只发射首先发射数据或通知的那个Observable的所有数据
     */
    @Test
    public void testAmb() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable<Integer> observable2 = Observable.just(4, 5, 6);
        Observable<Integer> observable3 = Observable.just(7, 8, 9);
        Observable.amb(Arrays.asList(
                observable1, observable2, observable3
        )).subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 设置默认值
     */
    @Test
    public void testDefaultEmpty() {
        Observable.empty().defaultIfEmpty(1).subscribe(data -> log.info("data:{}", data));

        //可以理解成guava的这个意思
        Optional.fromNullable(null).or(1);
    }

    /**
     * 挨个比较两个Observable发射的数据是否相同
     */
    @Test
    public void testSequenceEqual() {
        assertTrue(Observable
                .sequenceEqual(Observable.just(1, 2, 3), Observable.just(1, 2, 3))
                .blockingGet()
        );
    }

    @Test
    public void testSkipUntil() {
        Observable.just(1, 2, 3, 4, 5)
                .skipUntil(Observable.just(2))
                .subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 跳过满足条件的数据
     */
    @Test
    public void testSkipWhile() {
        Observable.just(1, 2, 3, 4, 5)
                .skipWhile(integer -> integer < 2)
                .subscribe(integer -> log.info("data:{}", integer));

        for (int i = 1; i < 6; i = i+1) {
            if (i < 2) {
                continue;
            }
            log.info("i:{}", i);
        }
    }

    /**
     * 直到满足条件之前都会继续接收数据。
     */
    @Test
    public void testTakeUntil() {
        Observable.just(1, 2, 3, 4, 5)
                .takeUntil(integer -> integer == 4)
                .subscribe(integer -> log.info("data:{}", integer));

        for (int i = 1; i < 6; i = i+1) {
            if (i == 4) {
                log.info("i:{}", i);
                break;
            }
            log.info("i:{}", i);
        }
    }

    /**
     * 有一次不满足条件就停止接收数据。
     */
    @Test
    public void testTakeWhile() {
        Observable.just(1, 2, 3, 4, 5)
                .takeWhile(integer -> integer < 3)
                .subscribe(integer -> log.info("data:{}", integer));


        for (int i = 1; i < 6; i = i+1) {
            if (i < 3) {
                log.info("i:{}", i);
                continue;
            }
            break;
        }
    }
}
