package common.zookeeper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ZookeeperDemoTest {


    private ZookeeperDemo zookeeperDemo = new ZookeeperDemo();

    @Test
    public void createSession() throws Exception {
        boolean create = zookeeperDemo.createSession();
        assertTrue(create);
    }

    @Test
    public void createDataNode() throws Exception {

        zookeeperDemo.createSession();

        zookeeperDemo.createDataNode("/test");

    }

    /**
     * 复用会话
     */
    @Test
    public void recoverSession() {
        boolean result = zookeeperDemo.recoverSession();
        assertTrue(result);
    }

    /**
     * 权限控制
     */
    @Test
    public void accessControl() {
        boolean result = zookeeperDemo.accessControl();
        assertTrue(result);
    }
}