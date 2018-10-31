package common.rxjava2;

import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class _16SubjectTest {

    /**
     * Subject:同时充当了Observer和Observable的角色。因为它是一个Observer，
     * 它可以订阅一个或多个 Observable;又因为它是一个Observable，它可以转发它收到(Observe)的数据，也可以发射 新的数据。
     */

    /**
     * AsyncSubject:无论输入多少参数，当完成的时候，永远只输出最后一个参数。
     */
    @Test
    public void testAsyncSubject() {
        AsyncSubject<Integer> source = AsyncSubject.create();
        source.onNext(1);
        source.onNext(2);
        source.subscribe(o -> log.info("test asyncSubject value-01:{}", o));

        source.onNext(3);
        source.onNext(4);
        source.subscribe(o -> log.info("test asyncSubject value-02:{}", o));
        source.onComplete();
    }

    /**
     * BehaviorSubject:会发送离订阅最近的上一个值，没有上一个值的时候会发送默认值。
     */
    @Test
    public void testBehaviorSubject() {
        BehaviorSubject<Integer> source = BehaviorSubject.create();
        source.subscribe(o -> log.info("test behaviorSubject value-01:{}", o));
        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        source.subscribe(o -> log.info("============ test behaviorSubject value-02:{}", o));
        source.onNext(4);
        source.onNext(5);
        source.onComplete();
    }

    /**
     * PublishSubject:可以说是最正常的Subject，从那里订阅就从那里开始发送数据。
     */
    @Test
    public void testPublishSubject() {
        PublishSubject source = PublishSubject.create();
        source.subscribe(o -> log.info("test publishSubject value-01:{}", o));
        source.onNext(1);
        source.onNext(2);

        source.subscribe(o -> log.info("============ test publishSubject value-02:{}", o));
        source.onNext(3);

        source.onComplete();
    }

    /**
     * ReplaySubject:无论何时订阅，都会将所有历史订阅内容全部发出
     */
    @Test
    public void testReplaySubject() {
        ReplaySubject source = ReplaySubject.create();
        source.subscribe(o -> log.info("test publishSubject value-01:{}", o));
        source.onNext(1);
        source.onNext(2);
        source.onNext(3);
        source.subscribe(o -> log.info("test publishSubject value-02:{}", o));

    }
}
