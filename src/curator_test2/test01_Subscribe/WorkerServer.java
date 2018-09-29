package curator_test2.test01_Subscribe;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * Created by zhengtengfei on 2018/9/28.
 */
public class WorkerServer {
    private CuratorFramework client;
    private NodeCache cache;
    private String ZKPathData;

    public WorkerServer(String address) {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000,1000);
        client = CuratorFrameworkFactory.
                builder()
                .connectString("0.0.0.0:2182")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();
    }

    public static void main(String[] args) throws InterruptedException {
        WorkerServer workerServer = new WorkerServer("/");
        workerServer.subscribeInfo();
        Thread.sleep(Integer.MAX_VALUE);
    }
    public void subscribeInfo() {
        cache = new NodeCache(client, "/sub");
        try {
            cache.start();
            if (cache.getCurrentData() != null) {
                if (!(new String(cache.getCurrentData().getData()).equals(""))) {
                    getZKPathData();
                }else{
                    System.out.println(".读取本地配置");
                }
            }else{
                System.out.println(".读取本地配置");
            }
            cache.getListenable().addListener(new NodeCacheListener() {
                public void nodeChanged() throws Exception {
                    System.out.println("zk 节点发生变化，重新读取配置");
                    getZKPathData();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getZKPathData() throws Exception {
        System.out.println(".读取zookeeper配置信息");
        byte[] data = client.getData().forPath("sub");
        ZKPathData = String.valueOf(data);
        return ZKPathData;
    }
}
