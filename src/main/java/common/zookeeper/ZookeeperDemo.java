package common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

@Slf4j
public class ZookeeperDemo {


    private static final Watcher WATCHER = watchedEvent -> {
        log.info("zookeeper session connect status, status:{}", watchedEvent.getState());
    };

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
                        return true;
                    } else {
                        Thread.sleep(1000);
                        log.info("zookeeper connecting, try again! status:{}", zooKeeper.getState());
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

}
