package common.java;

import org.junit.Test;

public class CommonTest {

    /**
     * 测试移位运算符
     */
    @Test
    public void test1() {
        int num = 32;
        System.out.println("测试数字：" + num + ";表示：" + Integer.toBinaryString(num));
        System.out.println("测试数字：" + num + ";右移：" + Integer.toBinaryString(num >> 1) + "；结果" + (num >> 1));
        System.out.println("测试数字：" + num + ";左移：" + Integer.toBinaryString(num << 1) + "；结果" + (num << 1));
        System.out.println("测试数字：" + num + ";左移：" + Integer.toBinaryString(num >>> 1) + "；结果" + (num >>> 1));
    }
}
