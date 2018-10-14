package common.guava;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class _4Ordering {

    private class Person {
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

    Comparator<Person> comparator = new Comparator<Person>() {
        public int compare(Person o1, Person o2) {
            return ComparisonChain.start()
                    .compare(o1.height, o2.height)
                    .compare(o1.weight, o2.weight).result();
        }
    };

    /**
     * 排序
     */
    @Test
    public void testSort() {

        Person p1 = new Person(123, 456);
        Person p2 = new Person(145, 456);
        Person p3 = new Person(145, 457);

        //使用指定的排序器
        Ordering<Person> reverse = Ordering.from(comparator)
                //使用当前排序器，但额外把null值排到最后面。
                .nullsLast()
                //获取语义相反的排序器
                .reverse();

        List<Person> list = Arrays.asList(p1, p2, p3);

        //排序 从高到矮，从胖到瘦
        System.out.println(reverse.sortedCopy(list));

        //实际查出是最矮的，原因是因为使用comparator去比较的，reverse没有生效
        Person max1 = reverse.reverse().max(list);
        Person min = reverse.reverse().min(list);

        System.out.println(max1);
        System.out.println(min);

    }
}
