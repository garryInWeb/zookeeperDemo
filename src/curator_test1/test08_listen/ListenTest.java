package curator_test1.test08_listen;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhengtengfei on 2018/9/20.
 */
public class ListenTest {
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

       final NodeCache cache = new NodeCache(client,"/gary/2");
       cache.start();
       cache.getListenable().addListener(() -> {
           byte[] ret = cache.getCurrentData().getData();
           System.out.println("new data:"+new String(ret));
       });

        Thread.sleep(Integer.MAX_VALUE);
    }

}
