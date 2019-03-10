package common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

@Slf4j
public class ZookeeperDemo {

    private ZooKeeper zooKeeper = null;

    private static final Watcher WATCHER = watchedEvent -> {
        log.info("zookeeper session connect status, status:{}", watchedEvent.getState());
    };

    /**
     * 创建会话
     * @return 是否创建成功
     */
    public boolean createSession() {
        String serverAddress = "127.0.0.1:2181";
        int timeout = 5000;
        try {
            //构造一个zookeeper会话，参数分别为：zookeeper服务的地址，会话建立的超时时间，watch的监听
            ZooKeeper zooKeeper = new ZooKeeper(serverAddress, timeout, WATCHER);
            log.info("zookeeper session create, status:{}", zooKeeper.getState());

            int tryCount = 5;
            while (tryCount > 0) {
                try {
                    if(ZooKeeper.States.CONNECTED == zooKeeper.getState()) {
                        log.info("zookeeper session establish! status:{}", zooKeeper.getState());
                        this.zooKeeper = zooKeeper;
                        return true;
                    } else {
                        log.info("zookeeper connecting, try again! status:{}", zooKeeper.getState());
                        Thread.sleep(1000);
                    }
                } finally {
                    tryCount --;
                }
            }
            log.info("zookeeper connect failed! status:{}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("zookeeper establish session error, address:{}, timeout:{}, e=", serverAddress, timeout, e);
        }
        return false;
    }

    /**
     * 创建数据节点
     */
    public void createDataNode(String dataPath) throws InterruptedException {

        AsyncCallback.StringCallback stringCallback = (rc, path, ctx, name) -> {
            /*
             * rc:result code[0:ok -4:ConnectionLoss 110:NodeExists -112:SessionExpired ]
             * path:接口调用时传入api的数据节点的节点路径参数值
             * ctx:接入调用时传入api的上下文
             * name:实际在节点创建的参数名
             */
            log.info("zookeeper create data node callback, rc:{}, path:{}, ctx:{}, name:{}", rc, path, ctx, name);
        };

        /*
         * path:创建的数据节点的路径
         * data[]:一个字节数组，是节点之后创建的内容
         * acl:节点的acl策略
         * createMode:节点类型[持久，持久顺序，临时，临时顺序]
         * cb:注册回调接口
         * ctx:上下文
         */
        zooKeeper.create(dataPath, "hello".getBytes(),
                ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT, stringCallback, "I'm context");

        Thread.sleep(1000000);

    }

    public void deleteDataNode(String dataPath, int version) {
        AsyncCallback.VoidCallback voidCallback = (rc, path, ctx)
                -> log.info("zookeeper delete data node callback, rc:{}, path:{}, ctx:{}", rc, path, ctx);
        zooKeeper.delete(dataPath, version, voidCallback, "i'm a context");

    }

    public void listDataNode(String dataPath) throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(dataPath, true);
        log.info("zookeeper query data node result:{}", children);
    }

    public void updateDataNode(String dataPath, String value, int version) {
        AsyncCallback.StatCallback statCallback = (rc, path, ctx, stat)
                -> log.info("zookeeper update data node callback, rc:{}, path:{}, ctx:{}, stat:{}", rc, path, ctx, stat);

        zooKeeper.setData(dataPath, value.getBytes(), version, statCallback, "I'm a update context");

    }



}
