package me.tm.serviceregistry.register.impl;

import me.tm.serviceregistry.register.ServiceRegistry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

@Component
public class ServiceRegistryImpl implements ServiceRegistry, Watcher {
    private final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private final int SESSION_TIMEOUT = 5000;
    private final String REGISTRY_PATH = "/registry";
    private CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk;

    private ServiceRegistryImpl() {}

    public ServiceRegistryImpl(String zkServers) {
        try {
            zk = new ZooKeeper(zkServers, SESSION_TIMEOUT, this);
            latch.await();
            logger.info("zk connected success");
        } catch (Exception ex) {
            logger.info("zk connected fail");
        }
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        try {
            // 创建根节点
            String registryPath = REGISTRY_PATH;
            if (zk.exists(registryPath, false) == null) {
                zk.create(registryPath, null, OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("create node: {}", registryPath);
            }

            // 创建服务节点
            String servicePath = REGISTRY_PATH + "/" + serviceName;
            if (zk.exists(servicePath, false) == null) {
                zk.create(servicePath, null, OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                logger.info("create node: {}", servicePath);
            }

            // 创建地址节点
            String addressPath = servicePath + "/" + "address-";
            if (zk.exists(addressPath, false) == null) {
                String addressNode= zk.create(addressPath, serviceAddress.getBytes(), OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                logger.info("create node: {}", addressNode);
            }
        } catch (Exception ex) {
            logger.error("create node failure",ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            latch.countDown();
        }
    }
}