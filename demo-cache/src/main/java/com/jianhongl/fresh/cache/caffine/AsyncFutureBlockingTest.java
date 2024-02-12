package com.jianhongl.fresh.cache.caffine;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试内容:
 */
public class AsyncFutureBlockingTest {
    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AsyncFutureBlockingTest.class);

    public static AtomicInteger cnt = new AtomicInteger(0);

    private static final AsyncCacheLoader<String, String> loaderCF = new AsyncCacheLoader<String, String>() {
        @Override
        public @NonNull CompletableFuture<String> asyncLoad(@NonNull String key, @NonNull Executor executor) {

            /**
             * 生成 asyncLoad的 CompletableFuture 需要较长的时间. 模拟使用NIO / 异步RPC在发送数据时产生了阻塞的场景.
             */
            CompletableFuture<String> x = new CompletableFuture<>();
            logger.info("在 asyncLoad 方法中同步阻塞的加载 [key={}]......", key);
            try {
                Thread.sleep(3000); // 模拟异步加载: 3s. 即模拟加载使用了3秒时间.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Future 生成 (key={}).....", key);
            executor.execute(()->{
                try {
                    Thread.sleep(3000); // 模拟异步加载: 3s. 即模拟加载使用了3秒时间.
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info("Future 完成");
                // 每次加1. 确保每次如果触发了load.值不一样.
                x.complete("[sleep后生成future" + cnt.incrementAndGet() + "]");
            });

            return x;
        }
    };

    /**
     * caffeine 缓存. 缓存时间默认为1秒. 验证数据在读取期间. 不计为缓存有效期.
     */
    private static final AsyncLoadingCache<String, String> demoCacheCF = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .maximumSize(2000)
            .buildAsync(loaderCF);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 第一次get采用异步加载. 这样不会阻塞主线程. 模拟触发首次数据加载.
        executorService.execute(() -> {
            try {
                logger.info("第一次获取缓存key:key1, 将触发缓存数据加载......");
                String data = demoCacheCF.get("key1").get();
                logger.info("[loadFinish] demoCacheCF.get(key1) --> {}", data);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        // 提交任务后,主线程sleep 10ms. 确保异步线程获取 key1 已经被触发执行.
        Thread.sleep(10);

        logger.info("main 线程中再次尝试获取key1 .....");

        long timeStart = System.currentTimeMillis();
        CompletableFuture<String> data = demoCacheCF.get("key1");
        long timeUsed = System.currentTimeMillis() - timeStart;

        // 这里大概花费的时间是3秒左右. 因为如果提交的任务没有执行延时. 则主线程的sleep 10ms不会影响 future生成时间. 因此大概对齐到 3秒
        // 此时,Future的状态实际还是未完成状态.
        logger.info("主线程中读取缓存Future结束.[key={}][future={}],timeUsed={} ms", "key1", data, timeUsed);

        timeStart = System.currentTimeMillis();
        String value = data.get();
        timeUsed = System.currentTimeMillis() - timeStart;

        // 这个日志说明: 缓存的future在生成后, 别人就可以获取到这个future.但是一直到真正完成时,获取到的都是未完成的future. 且这个时间是不会过期的.
        logger.info("future.get() key1 in main, value=>>{}<<,use {} ms", value, timeUsed);


        logger.info("load与读取测试结束. 等500ms,看缓存是否还有有效 (预期是有效)");
        Thread.sleep(500);
        // 开始读取
        timeStart = System.currentTimeMillis();
        data = demoCacheCF.get("key1");
        timeUsed = System.currentTimeMillis() - timeStart;
        // 此时缓存还没有失效.因此会很快得到value. 且是完成状态
        logger.info("主线程中读取缓存Future结束.[key={}][future={}],timeUsed={} ms", "key1", data, timeUsed);



        logger.info("load与读取测试结束. 等500ms,看缓存是否还有有效(预期是无效)");
        Thread.sleep(500);
        // 开始读取
        timeStart = System.currentTimeMillis();
        data = demoCacheCF.get("key1");
        timeUsed = System.currentTimeMillis() - timeStart;
        // 此时缓存已经失效.因此会再次触发加载
        logger.info("主线程中读取缓存Future结束.[key={}][future={}],timeUsed={} ms", "key1", data, timeUsed);

        // 此处数据获取还要再花费额外的 3 秒
        logger.info("value={}", data.get());

        logger.info("shut down the thread pool after all task finish....");
        executorService.shutdown();
        logger.info("finish test");
    }
}
