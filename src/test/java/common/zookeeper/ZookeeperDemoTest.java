package common.zookeeper;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZookeeperDemoTest {

    private ZookeeperDemo zookeeperDemo = new ZookeeperDemo();

    @Test
    public void createSession() throws Exception {

        boolean session = zookeeperDemo.createSession();

        assertTrue(session);
    }

}