package common.rxjava2;

import io.reactivex.Maybe;
import io.reactivex.MaybeOnSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 能够发射0或者1个数据，要么成功，要么失败。有点类似于Optional
 */
@Slf4j
public class _14MaybeTest {

    @Test
    public void testMaybe() {
        //只打印testA
        Maybe.create((MaybeOnSubscribe<String>) e -> {
            e.onSuccess("testA");
            e.onSuccess("testB");
        }).subscribe(s -> log.info("data:{}", s));

        //如果MaybeEmitter先调用了onComplete(),即使后面再调用了onSuccess()也不会发射任何数据。
        Maybe.create((MaybeOnSubscribe<String>) e -> {
            e.onComplete();
            e.onSuccess("testA");
        }).subscribe(s -> log.info("data:{}", s));
    }
}
