package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class _3FilterOptTest {

    /**
     * 仅在过了一段指定的时间还没发射数据时才发射一个数据
     */
    @Test
    public void testDebounce() {

    }

    /**
     * 抑制（过滤掉）重复的数据项
     */
    @Test
    public void testDistinct() {
        Observable.just(1, 2, 3, 1, 2, 3)
                .distinct().subscribe(integer -> log.info("data:{}", integer));
    }
    /**
     * 只发射第N项数据
     */
    @Test
    public void testElementAt() {
        Observable.just(1, 2, 3)
                .elementAt(1).subscribe(integer -> log.info("data:{}", integer));
        Observable.just(1, 2, 3)
                .elementAt(4).subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 只发射通过了谓词测试的数据项
     */
    @Test
    public void testFilter() {
        Observable.just(1, 2, 3).filter(data -> data > 2)
                .subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 只发射第一项（或者满足某个条件的第一项）数据
     */
    @Test
    public void testFirst() {
        Observable.just(1, 2, 3).first(4)
                .subscribe(integer -> log.info("data:{}", integer));

        Observable.empty().first(4)
                .subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 不发射任何数据，只发射Observable的终止通知
     */
    @Test
    public void testIgnoreElements() {
        Observable.just(1, 2, 3).ignoreElements()
                .doOnSubscribe(disposable -> log.info("no data"))
                .doOnComplete(() -> log.info("complete"));
    }

    /**
     * 只发射最后一项（或者满足某个条件的最后一项）数据
     */
    @Test
    public void testLast() {
        Observable.just(1, 2, 3).last(-4)
                .subscribe(integer -> log.info("data:{}", integer));

        Observable.empty().last(-4)
                .subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 定期发射Observable最近发射的数据项
     */
    @Test
    public void testSample() {

    }

    /**
     * 抑制Observable发射的前N项数据
     */
    @Test
    public void testSkip() {
        Observable.just(1, 2, 3, 4, 5).skip(2)
                .subscribe(integer -> log.info("data:{}", integer));

        log.info("=======");

        Observable.just(1, 2, 3, 4, 5).skip(-1)
                .subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 抑制Observable发射的后N项数据
     */
    @Test
    public void testSkipLast() {
        Observable.just(1, 2, 3, 4, 5).skipLast(2)
                .subscribe(integer -> log.info("data:{}", integer));

        log.info("=======");

        Observable.just(1, 2, 3, 4, 5).skipLast(-1)
                .subscribe(integer -> log.info("data:{}", integer));
    }

    /**
     * 只发射前面的N项数据
     */
    @Test
    public void testTake() {
        Observable.just(1, 2, 3, 4, 5).take(2)
                .subscribe(integer -> log.info("data:{}", integer));

        log.info("=======");

        /*Observable.just(1, 2, 3, 4, 5).take(-1)
                .subscribe(integer -> log.info("data:{}", integer));*/
    }

    /**
     * 发射Observable发射的最后N项数据
     */
    @Test
    public void testTakeLast() {
        Observable.just(1, 2, 3, 4, 5).takeLast(2)
                .subscribe(integer -> log.info("data:{}", integer));

        log.info("=======");

        /*Observable.just(1, 2, 3, 4, 5).takeLast(-1)
                .subscribe(integer -> log.info("data:{}", integer));*/
    }

}