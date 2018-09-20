package curator_test1.test09_childEvent;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryUntilElapsed;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_ADDED;

/**
 * Created by zhengtengfei on 2018/9/20.
 */
public class ChildEventTest {
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

        final PathChildrenCache cache = new PathChildrenCache(client,"/gary",true);
        cache.start();
        cache.getListenable().addListener((curatorFramework, pathChildrenCacheEvent) -> {
            switch (pathChildrenCacheEvent.getType()){
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED:"+pathChildrenCacheEvent.getData());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED:"+pathChildrenCacheEvent.getData());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED:"+pathChildrenCacheEvent.getData());
                    break;
                default:
                    break;
            }
        });

        Thread.sleep(Integer.MAX_VALUE);
    }

}
