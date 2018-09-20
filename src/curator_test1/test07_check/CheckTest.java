package curator_test1.test07_check;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhengtengfei on 2018/9/20.
 */
public class CheckTest {
    public static void main(String[] args) throws Exception {

        ExecutorService es = Executors.newFixedThreadPool(5);

        RetryPolicy retryPolicy = new RetryUntilElapsed(5000,1000);
        CuratorFramework client = CuratorFrameworkFactory.
                builder()
                .connectString("0.0.0.0:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        client.checkExists().inBackground((curatorFramework, curatorEvent) -> {
            Stat stat = curatorEvent.getStat();
            System.out.println(stat);
        },"123",es).forPath("/gary/2");

        Thread.sleep(Integer.MAX_VALUE);
    }
}
