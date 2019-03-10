package common.zookeeper;

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

}