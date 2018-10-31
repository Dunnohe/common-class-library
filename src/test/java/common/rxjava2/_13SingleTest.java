package common.rxjava2;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 只发射单个数据或错误事件。
 */
@Slf4j
public class _13SingleTest {

    /**
     * 你只能通过single发送单个数据，所有相关发送数据的方法都发送单个数据了
     */
    @Test
    public void testSingle() {
        Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                emitter.onSuccess(1);
            }
        }).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer integer) {
                log.info("data:{}", integer);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
