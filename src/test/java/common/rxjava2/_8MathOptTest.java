package common.rxjava2;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.Subject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.observables.MathObservable;

import static org.junit.Assert.assertTrue;

/**
 * 使用这个测试时，需要额外引入rxjava-math
 */
@Slf4j
public class _8MathOptTest {

    /**
     * 算平均值
     */
    @Test
    public void testAverage() {
    }

    /**
     * 判定一个Observable是否发射一个特定的值
     */
    @Test
    public void testConcat() {
        assertTrue(Observable.just(1, 2, 3, 4).contains(2).blockingGet());
    }

    @Test
    public void testReduce() {

    }

    @Test
    public void testMax() {

    }

    @Test
    public void testMin() {

    }

    @Test
    public void testCount() {

    }

    @Test
    public void testSum() {

    }
}
