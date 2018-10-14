package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * rx java2 所有创建运算符测试
 */
@Slf4j
public class _1CreateOptTest {

    /**
     * 1-测试创建运算符
     */
    @Test
    public void testCreate() {
        Observable.create((ObservableOnSubscribe<Integer>) observableEmitter -> {
            try {
                for (int i = 0; i < 5; i++) {
                    //这里如果不加，那么会发射所有的数据，尽管consumer不需要,
                    //为了不必要的消耗，我们可以加上这个判断。
                    if (!observableEmitter.isDisposed()) {
                        log.info("发射数据:{}", i);
                        observableEmitter.onNext(i);
                    }
                }
                observableEmitter.onComplete();
            } catch (Exception e) {
                observableEmitter.onError(e);
            }
        }).subscribe(new Observer<Integer>() {
            private Disposable selfDisposable;

            public void setSelfDisposable(Disposable selfDisposable) {
                this.selfDisposable = selfDisposable;
            }

            /**
             * 这个方法可以决定是否继续消费这个数据。
             * （实际这么说不贴切，而是改变处置数据的状态，这里便于理解我们这么说）
             */
            @Override
            public void onSubscribe(Disposable disposable) {
                setSelfDisposable(disposable);
            }

            @Override
            public void onNext(Integer integer) {
                //当接手的数据>=2的时候，我们不准备消费这个数据了
                if (integer >= 2) {
                    selfDisposable.dispose();
                }
                //接收数据时需要判断dispose，否则依然打印出>2的数据
                if (!selfDisposable.isDisposed()) {
                    log.info("消费数据:{}", integer);
                }

                if (integer == 1) {
                    throw new RuntimeException("test error");
                }
            }

            /**
             * 这个方法会捕捉到onNext的异常
             * 当发生异常时，后续的onNext也不会执行了。onComplete也不会执行了
             * @param throwable
             */
            @Override
            public void onError(Throwable throwable) {
                log.info("发生异常:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("执行完成");
            }
        });
    }

    public class App {
        private String name;

        public App(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "App{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    /**
     * 创建一个发射指定值的Observable
     */
    @Test
    public void testJust() {
        Observable.just(1, 2, 3, 4).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer integers) {
                log.info("consumer data:{}", integers);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }


    private App mApp = new App("微信");
    /**
     * 直到有观察者订阅时才创建Observable，并且为每个观察者创建一个新的Observable
     * 懒加载 保证数据是最新鲜的
     */
    @Test
    public void testDefer() {
        App app = new App("微信");
        Observable<App> observable = Observable.just(app);
        app = new App("QQ");
        observable.subscribe(new Observer<App>() {

            @Override
            public void onError(Throwable arg0) {
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(App a) {
                //这里会打印出"微信",但是如果我们希望打印出qq怎么实现？
                log.info("newest data:{}", a);
            }
        });


        //打印出QQ
        Observable<App> deferObservable = Observable.defer(new Callable<ObservableSource<? extends App>>() {
            @Override
            public ObservableSource<? extends App> call() throws Exception {
                return Observable.just(mApp);
            }
        });
        mApp = new App("QQ");
        deferObservable.subscribe(new Observer<App>() {

            @Override
            public void onError(Throwable arg0) {
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(App a) {
                log.info("newest data:{}", a);
            }
        });
    }

    /**
     * 创建一个不发射任何数据但是正常终止的Observable
     * onNext和onError不会执行,onComplete会执行
     */
    @Test
    public void testEmpty() {
        Observable.empty().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object o) {
                log.info("consumer data:{}", o);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * 创建一个不发射数据也不终止的Observable
     * onNext和onError和onComplete都不会执行
     */
    @Test
    public void testNever() {
        Observable.never().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object o) {
                log.info("consumer data:{}", o);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * 创建一个不发射数据以一个错误终止的Observable
     * onNext和onComplete不会执行,onError会执行
     */
    @Test
    public void testThrow() {
        Observable.error(new NullPointerException()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Object o) {
                log.info("consumer data:{}", o);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * 将普通的数据变成Observable模式
     */
    @Test
    public void testFrom() {
        //普通的数据
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        Observable.fromIterable(list).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer o) {
                log.info("consumer data:{}", o);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });

        log.info("===============================");

        Observable.fromArray(list).subscribe(new Observer<List<Integer>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(List<Integer> integers) {
                log.info("consumer data:{}", integers);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * 创建一个按固定时间间隔发射整数序列的Observable
     */
    @Test
    public void testInterval() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Long aLong) {
                log.info("current data:{}", aLong);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
        Thread.sleep(4000);
    }

    /**
     * 创建一个发射特定整数序列的Observable
     */
    @Test
    public void testRange() {
        //第一个参数是起始值，第二个参数是发射次数
        Observable.range(4, 7).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer integers) {
                log.info("consumer data:{}", integers);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * 创建一个发射特定数据重复多次的Observable
     */
    @Test
    public void testRepeat() {
        //just执行两次，当然这里也可以不是just，可以是其他任何东西
        Observable.just(1).repeat(2).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer integers) {
                log.info("consumer data:{}", integers);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });
    }

    /**
     * 返回一个Observable，它发射一个类似于函数声明的值
     * 它接受一个函数作为参数,调用这个函数获取一个值
     * 注意:这个函数只会被执行一次,即使多个观察者订阅这个返回的Observable。
     */
    @Test
    public void testStart() {
        //Observable.sta
    }

    /**
     * 创建一个Observable，它在一个给定的延迟后发射一个特殊的值。
     */
    @Test
    public void testTimer() throws InterruptedException {
        Observable.timer(1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Long value) {
                log.info("consumer data:{}", value);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("error:{}", throwable);
            }

            @Override
            public void onComplete() {
                log.info("complete");
            }
        });

        Thread.sleep(2000);
    }

}
