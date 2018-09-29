package curator_test2.test01_Subscribe;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;

/**
 * Created by zhengtengfei on 2018/9/28.
 */
public class PublishServer {
    public static final String SUB = "/sub";
    private CuratorFramework client;

    public static void main(String[] args) throws InterruptedException {
        PublishServer publishServer = new PublishServer();
        publishServer.publishInfo();
        Thread.sleep(Integer.MAX_VALUE);
    }

    public PublishServer() {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000,1000);
        client = CuratorFrameworkFactory.
                builder()
                .connectString("0.0.0.0:2182")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        try{
//            if (client.checkExists().forPath("/sub") == null){
//                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(SUB);
//            }
            if (client.checkExists().forPath("/sub") == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(SUB,"".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishInfo(){
        try{
            client.setData().forPath(SUB,"database".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
