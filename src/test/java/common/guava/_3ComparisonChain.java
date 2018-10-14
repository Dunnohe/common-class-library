package common.guava;

import com.google.common.collect.ComparisonChain;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class _3ComparisonChain {

    private class Person {

        //身高
        private int height;

        //体重
        private int weight;

        public Person(int height, int weight) {
            this.height = height;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "height=" + height +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * 比较
     * 实现先比较身高(从矮到高)，再比较体重（从轻到重）的逻辑
     */
    @Test
    public void testCompare() {

        Comparator<Person> c = new Comparator<Person>() {
            public int compare(Person o1, Person o2) {
                return ComparisonChain.start()
                        .compare(o1.height, o2.height)
                        .compare(o1.weight, o2.weight).result();
            }
        };

        Person p1 = new Person(123, 456);
        Person p2 = new Person(145, 456);
        Person p3 = new Person(145, 457);
        List<Person> list = Arrays.asList(p1, p2, p3);

        Collections.sort(list, c);
        System.out.println(list);
    }
}
