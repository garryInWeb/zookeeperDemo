package curator_test1.test10_master;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.curator.utils.CloseableUtils;

import java.util.Random;

/**
 * Created by zhengtengfei on 2018/9/26.
 */
public class WorkServer {

    private static final String MASTER_PATH = "/master";


    public WorkServer(){
        CuratorFramework client = null;
        final LeaderLatch latch;
        try {
            client = getClient();
            latch = new LeaderLatch(client,MASTER_PATH,String.valueOf(random()));
            latch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    System.out.println(latch.getId() + "leader");
                }

                @Override
                public void notLeader() {
                    System.out.println(latch.getId() + "not leader");
                }
            });
            latch.start();

            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    public static void main(String[] args) {
        WorkServer workServer = new WorkServer();
    }

    private CuratorFramework getClient() {
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

    private int random(){
        Random random = new Random();
        return random.nextInt(10);
    }

    public void doExecute(String id){
        System.out.println(id + "is run");
    }
}
