package common.guava;

import com.google.common.base.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.*;

public class _1OptionalTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * 测试创建一个optional实例
     */
    @Test
    public void testCreateInstance1() {
        String valueStr = "123";
        //创建一个optional实例,当valueStr为null会抛空指针异常。
        Optional optional = Optional.of(valueStr);
        //判断这个实例是否存在
        assertTrue(optional.isPresent());
    }

    /**
     * 测试创建一个optional实例，当value为空会抛异常
     */
    @Test
    public void testCreateInstance2() {
        //预测会抛空指针异常
        thrown.expect(NullPointerException.class);
        //抛空指针异常
        Optional.of(null);
    }

    /**
     * 测试创建一个缺失引用的optional实例
     */
    @Test
    public void testCreateInstance3() {
        Optional<Object> absent = Optional.absent();
        assertFalse(absent.isPresent());
    }

    /**
     * 测试创建可为空的optional实例
     */
    @Test
    public void test1() {
        //创建一个缺失引用的optional实例
        Optional<Object> objectOptional = Optional.fromNullable(null);

        //若引用缺失，返回指定的值
        Object or = objectOptional.or("123");

        assertTrue(!objectOptional.isPresent());

        assertTrue(or != null);

        //获得引用的值，如果值为空，则抛异常
        thrown.expect(IllegalStateException.class);
        objectOptional.get();


    }


}
