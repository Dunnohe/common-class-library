package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * @date: 2018/10/27 12:22
 * @description:
 */
@Slf4j
public class _10ConnextOptTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * ConnectableObservable：可连接的Observable在 被订阅时并不开始发射数据，只有在它的 connect() 被调用时才开始用这种方法，
     * 可以等所有的潜在订阅者都订阅了这个Observable之后才开始发射数据。
     * 即使没有任何订阅者订阅它，也可以使用 connect 让他发射
     * <p>
     * Publish:普通的Observable转换为可连接的Observable
     */
    @Test
    public void testConnectableObservable() {

        //使用publish将Observable转换成相应的ConnectableObservable操作
        ConnectableObservable<Integer> connectableObservable = Observable.just(1, 2, 3, 4, 5, 6).publish();

        log.info("=================订阅1====================================");
        connectableObservable.doOnSubscribe(disposable -> log.info("订阅1："))
                .subscribe(integer -> log.info("test connect01 value:{}", integer));

        log.info("=================订阅2====================================");
        connectableObservable.doOnSubscribe(disposable -> log.info("订阅2："))
                .subscribe(integer -> log.info("test connect02 value:{}", integer));
        //此时开始发射数据
        connectableObservable.connect();

        log.info("=================订阅3====================================");
        connectableObservable.doOnSubscribe(disposable -> log.info("订阅3："))
                .subscribe(integer -> log.info("test connect03 value:{}", integer));
        //此时不发射数据 只有等到连接才可消费
    }

    /**
     * RefCount：让一个可连接的Observable行为像普通的Observable
     */
    @Test
    public void testRefCount() {
        ConnectableObservable<Integer> connectableObservable = Observable.just(1, 2, 3, 4, 5, 6).publish();
        log.info("=================订阅1====================================");
        connectableObservable.subscribe(integer -> log.info("test connect01 value:{}", integer));
        //此时不发射数据 只有等到连接才可消费

        ConnectableObservable<Integer> connectableObservable2 = Observable.just(1, 2, 3, 4, 5, 6).publish();
        log.info("=================订阅2====================================");
        connectableObservable2.refCount().subscribe(integer -> log.info("test connect02 value:{}", integer));
    }

    /**
     * Replay:每次订阅 都对单个订阅的重复播放一边,保证每个消费者获取的数据序列一致
     * 总是发射完整的数据序列给任何未来的观察者，即使那些观察者在这个Observable开始给其它观察者发射数据之后才订阅
     */
    @Test
    public void testReplay() {
        //切记要让ConnectableObservable具有重播的能力,必须Obserable的时候调用replay,
        // 而不是ConnectableObservable 的时候调用replay
        ConnectableObservable<Integer> connectableObservable = Observable.just(1, 2, 3, 4, 5, 6).replay().publish();

        log.info("=================订阅1====================================");
        connectableObservable.doOnSubscribe(disposable -> log.info("订阅1："))
                .subscribe(integer -> log.info("test connect01 value:{}", integer));
        connectableObservable.connect();

        log.info("=================订阅2====================================");
        connectableObservable.doOnSubscribe(disposable -> log.info("订阅2："))
                .subscribe(integer -> log.info("test connect02 value:{}", integer));
    }

}
