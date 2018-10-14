package common.guava;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.google.common.base.Preconditions.*;

public class _2PreconditionsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    /**
     * 检查参数
     */
    @Test
    public void testParam() {
        Object obj = null;

        thrown.expect(NullPointerException.class);

        //会报空指针
        checkNotNull(obj);
    }

    /**
     * 检查参数
     */
    @Test
    public void testParam2() {
        Object obj = null;

        //符合预期
        checkArgument(obj == null);

        thrown.expect(IllegalArgumentException.class);
        //会报异常
        checkArgument(obj != null);
    }

    /**
     * 检查参数
     */
    @Test
    public void testParam3() {
        //符合预期
        checkState(true);

        thrown.expect(IllegalStateException.class);

        //会报异常
        checkState(false);
    }

}
