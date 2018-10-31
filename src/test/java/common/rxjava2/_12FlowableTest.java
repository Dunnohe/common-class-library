package common.rxjava2;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 能够发射0或n个数据，并以成功或错误事件终止。
 * 支持Backpressure，可以控制数据源发射的速度。
 * 需要用背压的就去用Flowable
 */
@Slf4j
public class _12FlowableTest {

    /**
     * 当上下游在不同的线程中，通过Observable发射，处理，响应数据流时，
     * 如果上游发射数据的速度快于下游接收处理数据的速度，
     * 这样对于那些没来得及处理的数据就会造成积压，
     * 这些数据既不会丢失，也不会被垃圾回收机制回收，
     * 而是存放在一个异步缓存池中，如果缓存池中的数据一直得不到处理，越积越多，
     * 最后就会造成内存溢出，
     * 这便是响应式编程中的背压（backpressure）问题。
     */
    @Test
    public void testFlowTest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                        System.out.println("发射----> 1");
                        e.onNext(1);
                        System.out.println("发射----> 2");
                        e.onNext(2);
                        System.out.println("发射----> 3");
                        e.onNext(3);
                        System.out.println("发射----> 完成");
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                //create方法中多了一个BackpressureStrategy类型的参数
                //为上下游分别指定各自的线程
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //onSubscribe回调的参数不是Disposable而是Subscription
                        s.request(Long.MAX_VALUE);
                        //注意此处，暂时先这么设置
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("接收----> " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("接收----> 完成");
                    }
                });

    }
}

