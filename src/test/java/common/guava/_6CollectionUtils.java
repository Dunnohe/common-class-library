package common.guava;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class _6CollectionUtils {

    /**
     * 集合接口     对应的Guava工具类
     * Collection  Collections2
     * List        Lists
     * Set         Sets
     * SortedSet   Sets
     * Map         Maps
     * SortedMap   Maps
     * Queue       Queues
     * Multiset    Multisets
     * Multimap    Multimaps
     * BiMap       Maps
     * Table       Tables
     * Iterable    Iterables
     */
    public void introduce() {

    }

    private class Dog {
        private int number;

        public Dog(int number) {
            this.number = number;
        }
    }

    /**
     * 测试Lists一些方法
     */
    @Test
    public void testLists() {
        //构造
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //构造长度为10的大小的数组
        ArrayList<Object> list2 = Lists.newArrayListWithCapacity(10);
        //构造长度为5+10+10/10=16个长度大小的数组
        ArrayList<Object> list3 = Lists.newArrayListWithExpectedSize(10);

        //转换
        List<Dog> dogs = Lists.transform(list, new Function<Integer, Dog>() {
            public Dog apply(@Nullable Integer integer) {
                return new Dog(integer);
            }
        });

        //按照3个数字一组拆分list
        List<List<Integer>> lists = Lists.partition(list, 3);

        //list反向
        Lists.reverse(list);

        // 1-dog 1
        // 2-dog 2
        // 3-dog 3
        ImmutableMap<Integer, Dog> map = Maps.uniqueIndex(dogs, new Function<Dog, Integer>() {
            public Integer apply(@Nullable _6CollectionUtils.Dog dog) {
                return dog.number;
            }
        });
    }

    /**
     * 测试Maps一些方法
     */
    @Test
    public void testMaps() {
        Maps.newHashMap();

        Map<String, Integer> left = ImmutableMap.of("a", 1, "b", 2);
        Map<String, Integer> right = ImmutableMap.of("d", 5, "b", 2);
        MapDifference<String, Integer> diff = Maps.difference(left, right);
        System.out.println(diff.entriesInCommon());// {"b" => 2}
        System.out.println(diff.entriesOnlyOnLeft()); // {"a" => 1}
        System.out.println(diff.entriesOnlyOnRight()); // {"d" => 5}
    }


    /**
     * Sets
     */
    @Test
    public void testSets() {
        ImmutableSet<Integer> left = ImmutableSet.of(1, 2);
        ImmutableSet<Integer> right = ImmutableSet.of(2, 3);

        //并集  [1, 2, 3]
        System.out.println(Sets.union(left, right));
        //交集  [2]
        System.out.println(Sets.intersection(left, right));
        //差集 left - right  [1]
        System.out.println(Sets.difference(left, right));
        //差集 right - left  [3]
        System.out.println(Sets.difference(right, left));
    }
}
