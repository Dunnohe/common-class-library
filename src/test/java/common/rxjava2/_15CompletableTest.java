package common.rxjava2;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 它从来不发射数据，只处理 onComplete 和 onError 事件。可以看成是Rx的Runnable。
 */
@Slf4j
public class _15CompletableTest {

    @Test
    public void testCompletable() {

        //你是不能利用Completable发送任何数据的，因为发送数据的方法都被隐藏了起来
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                log.info("想发射数据但是发射不了");
                emitter.onComplete();
            }
        }).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                log.info("没法接收数据，只能完成了");
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
