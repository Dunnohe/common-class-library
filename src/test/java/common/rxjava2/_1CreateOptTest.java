package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * rx java2 所有创建运算符测试
 */
@Slf4j
public class _1CreateOptTest {

    /**
     * 测试创建运算符
     */
    @Test
    public void testCreate() {
        Observable.create((ObservableOnSubscribe<Integer>) observableEmitter -> {
            try {
                for (int i = 0; i < 5; i++) {
                    log.info("[test create]sss");
                    observableEmitter.onNext(i);
                }
                observableEmitter.onComplete();
            } catch (Exception e) {
                observableEmitter.onError(e);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("[onNext] integer:" + integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("[error] err:" + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("[complete]");
            }
        });

    }
}
