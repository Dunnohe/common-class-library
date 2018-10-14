package common.guava;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class _5Collections {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testImmutable() {
        //声明不可变map
        ImmutableMap<Integer, String> immutableMap = ImmutableMap.of(1, "123", 2, "123");
        //声明不可变map
        ImmutableMap<Integer, String> immutableMap2 = ImmutableMap.<Integer, String>builder()
                .put(1, "123")
                .put(2, "123").build();

        //声明不可变set
        ImmutableSet<Integer> immutableSet = ImmutableSet.of(1, 2, 3);
        //声明不可变set
        ImmutableSet<Integer> immutableSet2 = ImmutableSet.<Integer>builder().add(1).add(2).build();
        //声明不可变List
        ImmutableList<Integer> immutableList = ImmutableList.of(1, 2, 3);

        thrown.expect(UnsupportedOperationException.class);
        immutableList.add(1);
    }

    /**
     * HashMap<String, int> == HashMultiset
     * TreeMap<String, int> == TreeMultiset
     * LinkedHashMap<String, int> == LinkedHashMultiset
     * ConcurrentHashMap<String, int> == ConcurrentHashMultiset
     * ImmutableMap<String, int> == ImmutableMultiset
     */
    @Test
    public void testMultiset() {
        ImmutableList<String> words = ImmutableList.of("123", "123", "abc", "dfg", "wer");

        //计算单次出现的次数 老方法:
        Map<String, Integer> counts = Maps.newHashMap();
        for (String word : words) {
            Integer count = counts.get(word);
            if (count == null) {
                counts.put(word, 1);
            } else {
                counts.put(word, count + 1);
            }
        }
        System.out.println(counts.get("123"));

        //计算单次出现的次数 新方法:
        HashMultiset<String> hashMultiset = HashMultiset.create(words);
        System.out.println(hashMultiset.count("123"));


    }


    /**
     * multi map用于实现一对多的结构存储
     * HashMap<String, HashSet<String>> == HashMultimap<String, String>
     * ArrayList<String, ArrayList<String>> == ArrayListMultimap<String, String>
     * LinkedHashMap<String, LinkedList<string>> == LinkedListMultimap<String, String>
     * LinkedHashMap<String, LinkedHashMap<string>> == LinkedHashMultimap<String, String>
     * TreeMap<String, TreeSet<string>> == TreeMultimap<String, String>
     * ImmutableMap<String, ImmutableList<string>> == ImmutableListMultimap<String, String>
     * ImmutableMap<String, ImmutableSet<string>> == ImmutableSetMultimap<String, String>
     */
    @Test
    public void testNewMultimap() {
        //Map<String, Hash<String>> == ArrayListMultimap<String, String>
        HashMultimap<String, String> hashMultimap = HashMultimap.create();

        //Map<String, List<String>> == MultiList<String, String>
        ArrayListMultimap<Integer, String> listMultimap = ArrayListMultimap.create();
        listMultimap.put(1, "123");
        listMultimap.put(1, "123");
        listMultimap.put(1, "123");

        //获取1的集合
        List<String> strings = listMultimap.get(1);

        //摊开
        Map<Integer, Collection<String>> integerCollectionMap = listMultimap.asMap();

        //用Multiset表示Multimap中的所有键
        Multiset<Integer> keys = listMultimap.keys();

        //用Set表示Multimap中所有不同的键。
        Set<Integer> integers = listMultimap.keySet();

        //将所有的value collection合成一个collection返回
        Collection<String> values = listMultimap.values();

        //增删改查都有

    }


    /**
     * table用于实现表格的存储
     * HashMap<String, HashMap<String, String>> == HashBasedTable<String, String, String>
     * TreeMap<String, TreeMap<String, String>> == TreeBasedTable<String, String, String>
     * ImmutableMap<String, ImmutableMap<String, String>> == ImmutableTable<String, String, String>
     * 二维数组 == ArrayTable<String, String, String>  要求在构造时就指定行和列的大小
     */
    @Test
    public void testTable() {
        HashBasedTable<String, String, String> table = HashBasedTable.create();
        //放入行x1, 列y1，值1
        table.put("x1", "y1", "1");

        //获得行x1，列y1的值
        table.get("x1", "y1");

        //获得行x1上面的所有集合
        System.out.println(table.row("x1"));

        //获得所有行key
        System.out.println(table.rowKeySet());

        //获得行y1上面的所有集合
        System.out.println(table.column("y1"));

        //获得所有列key
        System.out.println(table.columnKeySet());

    }

    /**
     * bi map用于实现键键映射
     * 比如userId是唯一的，username也是唯一的，我们可以建立userId到username的映射，
     * 可以实现根据username获取userId，也可以根据userId获取username,这就是bi map的使用场景
     * 键–值实现 HashMap 值–键实现 HashMap == HashBiMap
     * 键–值实现 ImmutableMap 值–键实现 ImmutableMap == ImmutableBiMap
     * 键–值实现 EnumMap 值–键实现 EnumMap == EnumBiMap
     * 键–值实现 EnumMap 值–键实现 HashMap == 	EnumHashBiMap
     *
     */
    @Test
    public void testBiMap() {
        HashBiMap<Integer, String> biMap = HashBiMap.create();
        biMap.put(1, "123");
        biMap.put(2, "456");
        biMap.put(3, "789");

        //根据userId获取username
        System.out.println(biMap.get(1));
        //根据username获取userId
        System.out.println(biMap.inverse().get("123"));


        //这么操作是不行的，不能用username去重新映射，因为userid已经存在了。
        //thrown.expect(IllegalArgumentException.class);
        //biMap.inverse().put("1234", 1);

        biMap.inverse().forcePut("1234", 1);
        //根据userId获取username
        System.out.println(biMap.get(1));
    }

    /**
     * 当表示区间的时候使用range set
     * 闭区间 [1, 10] = {x | a <= x <= b}
     * 开区间 (1, 10) = {x | a < x < b}
     *
     */
    @Test
    public void testRangeMap() {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();
        // {[1,10]}
        rangeSet.add(Range.closed(1, 10));
        //不相连区间:{[1,10], [11,15)}
        rangeSet.add(Range.closedOpen(11, 15));
        //相连区间; {[1,10], [11,20)}
        rangeSet.add(Range.closedOpen(15, 20));
        //空区间; {[1,10], [11,20)}
        rangeSet.add(Range.openClosed(0, 0));
        //分割[1, 10]; {[1,5], [10,10], [11,20)}
        rangeSet.remove(Range.open(5, 10));

        //RangeSet的实现支持非常广泛的视图：
        //complement()：返回RangeSet的补集视图。complement也是RangeSet类型,包含了不相连的、非空的区间。
        //subRangeSet(Range<C>)：返回RangeSet与给定Range的交集视图。这扩展了传统排序集合中的headSet、subSet和tailSet操作。
        //asRanges()：用Set<Range<C>>表现RangeSet，这样可以遍历其中的Range。

        //RangeSet的查询方法
        //为了方便操作，RangeSet直接提供了若干查询方法，其中最突出的有:
        //contains(C)：RangeSet最基本的操作，判断RangeSet中是否有任何区间包含给定元素。
        //rangeContaining(C)：返回包含给定元素的区间；若没有这样的区间，则返回null。
        //encloses(Range<C>)：简单明了，判断RangeSet中是否有任何区间包括给定区间。
        //span()：返回包括RangeSet中所有区间的最小区间。

    }


}
