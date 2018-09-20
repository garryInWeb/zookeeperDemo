package curator_test1.test06_set;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;

/**
 * Created by zhengtengfei on 2018/9/20.
 */
public class SetTest {
    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000,1000);
        CuratorFramework client = CuratorFrameworkFactory.
                builder()
                .connectString("0.0.0.0:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        Stat stat = new Stat();

        byte[] ret = client.getData().storingStatIn(stat).forPath("/gary/2");

        client.setData().withVersion(stat.getVersion()).forPath("/gary/2","456".getBytes());
        Thread.sleep(Integer.MAX_VALUE);
    }
}
