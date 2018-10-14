package common.guava;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class _8ListenerFuture {
    public class Person {
        //身高
        private int height;
        //体重
        private int weight;

        public Person(int height, int weight) {
            this.height = height;
            this.weight = weight;
        }

        public int getHeight() {
            return height;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "height=" + height +
                    ", weight=" + weight +
                    '}';
        }
    }

    @Test
    public void test() {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Person> personListenableFuture = service.submit(new Callable<Person>() {
            public Person call() {
                System.out.println("call");
                return null;
            }
        });

        Futures.addCallback(personListenableFuture, new FutureCallback<Person>() {
            @Override
            public void onSuccess(@Nullable Person object) {
                System.out.println("call success");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("call fail");
            }
        }, service);

    }
}
