package common.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: linhu
 * @date: 2018/10/14 14:15
 * @description: 转换操作
 */
@Slf4j
public class _2TransformingOptTest {

    /**
     * buffer操作符：定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值
     */
    @Test
    public void testBuffer() {
        // 1.buffer(count)
        // 以列表(List)的形式发射非重叠的缓存，每一个缓存至多包含来自原始Observable的count项数据（最后发射的列表数据可能少于count项）
        Observable.just(1, 2, 3, 4, 5, 6, 7).
                buffer(3).
                subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        log.info("test buffer(count) size:{},values:{}", integers.size(), integers);
                    }
                });


        // 2.buffer(count,skip)
        // 作用是将 Observable 中的数据按 skip (步长) 分成最大不超过 count 的 buffer ，然后生成一个  Observable
        Observable.just(1, 2, 3, 4, 5, 6).
                buffer(4, 1).
                subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        log.info("test buffer(count,skip) size:{},values:{}", integers.size(), integers);
                    }
                });
    }

    /**
     * Window 定期将来自原始Observable的数据分解为一个Observable窗口，发射这些窗口，而不是每次发射一项数据
     * 与buffer一样，区别是Window取自Observables对象，但是buffer取得是数据包
     */
    @Test
    public void testWindow() {
        Observable.just(1, 2, 3, 4, 5, 6).
                window(3)
                .subscribe(integerObservable -> {
                    integerObservable.subscribe(integer -> log.info(integerObservable + "===>" + integer));
                });
    }

    public class Student implements Callable<Student> {
        //姓名
        private String name;

        //选修课列表
        private List<String> courses;

        public Student(String name) {
            this.name = name;
        }

        public void setCourses(List<String> courses) {
            this.courses = courses;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", courses=" + courses +
                    '}';
        }

        @Override
        public Student call() throws Exception {
            return this;
        }
    }

    /**
     * Map  对Observable发射的每一项数据应用一个函数，执行变换操作
     */
    @Test
    public void testMap() {
        Observable.just(new Student("tom"), new Student("jerry"), new Student("jack"))
                .map(new Function<Student, Student>() {
                    @Override
                    public Student apply(Student student) throws Exception {
                        student.setCourses(Arrays.asList("chinese", "math"));
                        return student;
                    }
                }).subscribe(new Consumer<Student>() {
            @Override
            public void accept(Student student) throws Exception {
                log.info("test Map value:{}", student);
            }
        });
    }



    /**
     * FlatMap将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据处理合并后放进一个单独的Observable
     * 但有个需要注意的是，flatMap 并不能保证事件的顺序，如果需要保证，需要用到ConcatMap。它与FlatMap唯一区别就是保证处理后的顺序
     * https://www.jianshu.com/p/3e5d53e891db
     */
    @Test
    public void testFlatMap() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>());

        Observable.just(new Student("tom"), new Student("jerry"), new Student("jack"))
                .flatMap(new Function<Student, ObservableSource<Student>>() {
                    @Override
                    public ObservableSource<Student> apply(Student student) throws Exception {
                        return Observable.fromCallable(new Callable<Student>() {
                            @Override
                            public Student call() throws Exception {
                                student.setCourses(Arrays.asList("chinese", "math"));
                                if(student.name.equals("tom")) {
                                    for (int i = 0; i < 10000000; i++) {

                                    }
                                }
                                return student;
                            }
                        }).subscribeOn(Schedulers.from(executorService));
                    }
                })
                .subscribe(new Consumer<Student>() {
            @Override
            public void accept(Student strings) throws Exception {
                log.info("test flatMap : value : {}", strings);
            }
        });


        //消费异步
        /*Observable.just(new Student("tom"), new Student("jerry"), new Student("jack"))
                .flatMap(new Function<Student, ObservableSource<Student>>() {
                    @Override
                    public ObservableSource<Student> apply(Student student) throws Exception {
                        return Observable.just(student);

                    }
                }).observeOn(Schedulers.from(executorService)).subscribe(new Consumer<Student>() {
            @Override
            public void accept(Student strings) throws Exception {
                log.info("test flatMap : value : {}", strings);
            }
        });*/

        Thread.sleep(5000);
    }

    /**
     * GroupBy 将一个Observable分拆为一些Observables集合，它们中的每一个发射原始Observable的一个子序列
     */
    @Test
    public void testGroupBy() {
        Observable.just(1, 2, 3, 4, 5, 6).
                groupBy(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer % 3;
                    }
                }, new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "(" + integer + ")";
                    }
                }).
                subscribe(new Consumer<GroupedObservable<Integer, String>>() {
                    @Override
                    public void accept(GroupedObservable<Integer, String> group) throws Exception {
                        group.subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String signGroup) throws Exception {
                                log.info("test group by key:{},values:{}", group.getKey(), signGroup);
                            }
                        });
                    }
                });

        Observable.just(1, 2, 3, 4, 5, 6).groupBy(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer % 3;
            }
        }, new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return String.valueOf(integer + "@");
            }
        }).subscribe(new Observer<GroupedObservable<Integer, String>>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(GroupedObservable<Integer, String> groupedObservable) {
                groupedObservable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        log.info("key:{} value:{}", groupedObservable.getKey(), s);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Scan连续地对数据序列的每一项应用一个函数，然后对每一项连续发射输出结果
     */
    @Test
    public void testScan() {
        Observable.just(1, 2, 3, 4, 5, 6).
                scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer output, Integer input) throws Exception {
                        log.info("上次执行的结果:{}, 本次输入:{}", output, input);
                        return input + output;
                    }
                }).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log.info("test scan value:{}", integer);
                    }
                });
    }

    /**
     * Reduce连续对数据序列每一项应用一个函数，但是区别于Scan，只是输出最后处理结果
     */
    @Test
    public void testReduce() {
        Observable.just(1, 2, 3, 4, 5, 6).
                reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).
                subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        log.info("test reduce value:{}", integer);
                    }
                });
    }

}
