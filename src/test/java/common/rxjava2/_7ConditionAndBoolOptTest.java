package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class _7ConditionAndBoolOptTest {

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
        obs.onErrorResumeNext(Observable.just("hello")).subscribe(obj -> log.info("data:{}", obj));
        obs.onExceptionResumeNext(Observable.just("hello")).subscribe(obj -> log.info("data:{}", obj));
    }


    @Test
    public void testRetry() {
        Observable<Object> obs = Observable
                .create(sub -> {
                    for (int i = 0; i < 10; i++) {
                        if (i == 1) {
                            sub.onError(new RuntimeException("error"));
                        }
                        sub.onNext(i);
                    }
                });

        obs.retry((time, ex) -> time != 2 || !(ex instanceof RuntimeException))
                .subscribe(obj -> log.info("data:{}", obj));
    }
}
