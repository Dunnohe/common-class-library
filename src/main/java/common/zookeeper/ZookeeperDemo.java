package common.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

@Slf4j
public class ZookeeperDemo {

    private Long sessionId;
    private byte[] password;
    private ZooKeeper zooKeeper;
    private String serverAddress = "127.0.0.1:2181";
    private int timeout = 5000;


    private static final Watcher WATCHER = watchedEvent -> {
        log.info("zookeeper session connect status,path:{}, status:{}", watchedEvent.getPath(), watchedEvent.getState());
    };

    /**
     * 创建会话
     * 构造一个zookeeper会话，参数分别为：zookeeper服务的地址，会话建立的超时时间，watch的监听
     *
     * @return
     */
    public boolean createSession() {
        try {

            zooKeeper = new ZooKeeper(serverAddress, timeout, WATCHER);
            log.info("zookeeper session create, status:{}", zooKeeper.getState());

            int tryCount = 5;
            while (tryCount > 0) {
                try {
                    if (ZooKeeper.States.CONNECTED == zooKeeper.getState()) {
                        log.info("zookeeper session establish! status:{}", zooKeeper.getState());
                        sessionId = zooKeeper.getSessionId();
                        password = zooKeeper.getSessionPasswd();
                        return true;
                    } else {
                        log.info("zookeeper connecting, try again! status:{}", zooKeeper.getState());
                        Thread.sleep(1000);
                    }
                } finally {
                    tryCount--;
                }
            }
            log.info("zookeeper connect failed! status:{}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("zookeeper establish session error, address:{}, timeout:{}, e=", serverAddress, timeout, e);
        }

        return false;
    }

    /**
     * 复用会话
     * 复用一个zookeeper会话，参数分别为：zookeeper服务的地址，会话建立的超时时间，watch的监听，sessionId,password
     *
     * @return
     */
    public boolean recoverSession() {
        try {
            log.info("before reuse zookeeper session,status:{}", zooKeeper.getState());

            zooKeeper = new ZooKeeper(serverAddress, timeout, WATCHER, 1, "test".getBytes());
            Thread.sleep(1000);
            log.info("illegal zookeeper session,status:{}", zooKeeper.getState());

            zooKeeper = new ZooKeeper(serverAddress, timeout, WATCHER, sessionId, password);
            Thread.sleep(1000);
            log.info("after reuse zookeeper session, status:{}", zooKeeper.getState());

            return true;
        } catch (Exception e) {
            log.error("reuse session is error,address:{},error:", serverAddress, e);
        }
        return false;
    }

    /**
     * 权限控制
     * add auth info 方法中的scheme相当于权限控制模式：world、auth、digest、ip、super auth:具体的权限信息 在这里相当于秘钥
     * world：默认方式，相当于全世界都能访问
     * auth：代表已经认证通过的用户(cli中可以通过addauth digest user:pwd 来添加当前上下文中的授权用户)
     * digest：即用户名:密码这种方式认证，这也是业务系统中最常用的
     * ip:使用IP地址的方式进行访问
     * @return
     */
    public boolean accessControl() {
        String PATH = "/zk-book-auth-test";
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        try {
            zooKeeper.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
            Thread.sleep(1000);

            //错误的权限进行访问
            ZooKeeper errorAuthZookeeper = new ZooKeeper(serverAddress, timeout, WATCHER);
            errorAuthZookeeper.addAuthInfo("digest", "foo:false".getBytes());
            Thread.sleep(1000);
            try {
                String data = errorAuthZookeeper.getData(PATH, WATCHER, null).toString();
                log.info("error auth zookeeper get data:{}", data);
            } catch (Exception e) {
                log.error("error auth zookeeper get data,e=", e);
            }

            //授权正确的值进行访问
            ZooKeeper trueAuthZookeeper = new ZooKeeper(serverAddress, timeout, WATCHER);
            trueAuthZookeeper.addAuthInfo("digest", "foo:true".getBytes());
            Thread.sleep(1000);
            try {
                String data = trueAuthZookeeper.getData(PATH, WATCHER, null).toString();
                log.info("true auth get data:{}", data);
            } catch (Exception e) {
                log.info("true auth zookeeper get data,e=", e);
            }

            //没有授权进行访问
            ZooKeeper noAuthZooKeeper = new ZooKeeper(serverAddress, timeout, WATCHER);
            Thread.sleep(1000);
            try {
                String data = noAuthZooKeeper.getData(PATH, WATCHER, null).toString();
                log.info("no auth get data:{}", data);
            } catch (Exception e) {
                log.info("no auth zookeeper get data,e=", e);
            }

            return true;
        } catch (Exception e) {
            log.error("access control is error,e=", e);
        }
        return false;
    }

}
