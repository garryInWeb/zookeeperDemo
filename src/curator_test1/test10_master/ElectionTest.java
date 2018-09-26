package curator_test1.test10_master;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.utils.CloseableUtils;

import java.util.Random;

/**
 * Created by zhengtengfei on 2018/9/26.
 */
public class ElectionTest {

    private static final String MASTER_PATH = "/master";

    public static void main(String[] args) {
        CuratorFramework client = null;
        try {
            client = getClient();
            int count = 0;
            final String name = String.valueOf(random() + " client");
            LeaderSelector leaderSelector = new LeaderSelector(client, MASTER_PATH, new LeaderSelectorListener() {
                @Override
                public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                    System.out.println(name + "get leader");
                    Thread.sleep(2000);
                }

                @Override
                public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                    System.out.println(name + "changed state");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            leaderSelector.autoRequeue();
            leaderSelector.start();

            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }
    private static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000,1000);
        CuratorFramework client = CuratorFrameworkFactory.
                builder()
                .connectString("0.0.0.0:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();
        return client;
    }

    private static int random(){
        Random random = new Random();
        return random.nextInt(10);
    }
}
