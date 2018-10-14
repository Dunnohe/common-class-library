package common.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class _7Strings {

    @Test
    public void testSplit() {
        String str = "aaa,bbb,ccc";

        List<String> strings = Splitter.on(",")
                //移除结果字符串的前导空白和尾部空白
                .trimResults()
                //从结果中自动忽略空字符串
                .omitEmptyStrings().splitToList(str);

        System.out.println(strings);
    }

    @Test
    public void testJoin() {
        List<String> strings = Arrays.asList("123", "456", "678", null);

        String join = Joiner.on(",")
                .skipNulls().join(strings);

        System.out.println(join);
    }


}
