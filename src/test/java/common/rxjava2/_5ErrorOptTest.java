package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * 异常处理操作
 */
@Slf4j
public class _5ErrorOptTest {

    /**
     * 异常处理，catch操作符有三个操作：
     * onErrorReturn：让Observable遇到错误时发射一个特殊的项并且正常终止。
     * onErrorResumeNext：当抛出Throwable时返回一个新的Observable
     * onExceptionResumeNext：同onErrorResumeNext但只有抛出Exception时才会触发
     */
    @Test
    public void testCatch() {
        Observable<Object> obs = Observable.create(sub -> {
            for (int i = 0; i < 10; i++) {
                if (i == 1) {
                    sub.onError(new Throwable("123"));
                    //sub.onError(new RuntimeException("123"));
                }
                sub.onNext(i);
            }
        });

        obs.onErrorReturn((Function<Throwable, Integer>) throwable -> {
            log.info("error");
            return -1;
        }).subscribe(integer -> log.info("data:{}", integer));

        log.info("=======");
        obs.onErrorResumeNext(Observable.just("hello")).subscribe(obj -> log.info("data:{}", obj));

        log.info("=======");
        obs.onExceptionResumeNext(Observable.just("hello")).subscribe(obj -> log.info("data:{}", obj));
    }

    @Test
    public void testEx() {
        IOException ioException = new IOException("123");
        Throwable throwable = ioException;
        log.info("data:{}", throwable instanceof RuntimeException);
    }

    /**
     * repeat and retry difference
     * repeat() resubscribes when it receives onCompleted().
     * retry() resubscribes when it receives onError().
     */
    @Test
    public void testRetry() {
        Observable<Integer> obs = Observable
                .create(sub -> {
                    for (int i = 0; i < 10; i++) {
                        if (i == 1) {
                            sub.onError(new IOException("error"));
                        }
                        sub.onNext(i);
                    }
                });

        obs.retry(2, new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                return throwable instanceof IOException;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer o) {
                log.info("data:{}", o);
            }

            @Override
            public void onError(Throwable e) {
                log.error("err");
            }

            @Override
            public void onComplete() {

            }
        });




    }


}
