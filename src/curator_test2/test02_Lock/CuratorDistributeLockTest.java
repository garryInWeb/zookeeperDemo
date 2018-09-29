package curator_test2.test02_Lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryUntilElapsed;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by zhengtengfei on 2018/9/29.
 */
public class CuratorDistributeLockTest {
    public static int random = new Random().nextInt(10);

    public static void main(String[] args) {

        String lockPath = "/distribute-lock";
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000,1000);
        CuratorFramework client = CuratorFrameworkFactory.
                builder()
                .connectString("0.0.0.0:2182")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, lockPath);

        for (; true ;){
            process(lock);
        }
    }
    private static void process(InterProcessMutex lock){
        System.out.println(Thread.currentThread().getName() + "acquire");
        try{
            lock.acquire();
            System.out.println(LocalDateTime.now());
            System.out.println(random + "acquire success");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread().getName() + "release");
            try{
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
