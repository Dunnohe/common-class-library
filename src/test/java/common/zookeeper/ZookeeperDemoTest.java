package common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
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

        // success
        zookeeperDemo.createDataNode("/aaa", "hello");
        zookeeperDemo.createDataNode("/aaa/bbb", "hello/bbb");
        zookeeperDemo.createDataNode("/ddd", "hello/bbb");

        // fail
        zookeeperDemo.createDataNode("/ccc/bbb", "hello/bbb");
    }

    @Test
    public void listDataNode() throws Exception {

        zookeeperDemo.createSession();
        zookeeperDemo.listDataNode("/");
        zookeeperDemo.listDataNode("/aaa");

    }

    @Test
    public void getDataNode() throws Exception {
        zookeeperDemo.createSession();
        Stat stat = new Stat();
        zookeeperDemo.getDateNode("/aaa", stat);

    }

    @Test
    public void updateDataNode() throws Exception {
        zookeeperDemo.createSession();
        Stat stat = new Stat();
        zookeeperDemo.getDateNode("/aaa", stat);
        zookeeperDemo.updateDataNode("/aaa", "bye-bye", stat.getVersion());
        zookeeperDemo.getDateNode("/aaa", stat);
    }

    @Test
    public void deleteDataNode() throws Exception {
        zookeeperDemo.createSession();
        Stat stat = new Stat();
        zookeeperDemo.getDateNode("/aaa/bbb", stat);
        zookeeperDemo.deleteDataNode("/aaa/bbb", stat.getVersion());
        zookeeperDemo.getDateNode("/aaa/bbb", stat);
        zookeeperDemo.getDateNode("/aaa", stat);
    }

    /**
     * 复用会话
     */
    @Test
    public void recoverSession() {
        zookeeperDemo.createSession();
        zookeeperDemo.recoverSession();
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