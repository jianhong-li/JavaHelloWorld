package com.jianhongl.fresh.cache.caffine;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 用于验证一个未完成的Future, 已经在 asyncLoad函数中返回后, 是可以立即被其它的缓存get获取到.且是同一个future.
 */
public class AsyncLoadBlockingTest {
    private static final AsyncCacheLoader<String, String> loaderCF = new AsyncCacheLoader<String, String>() {
        @Override
        public @NonNull CompletableFuture<String> asyncLoad(@NonNull String key, @NonNull Executor executor) {

            CompletableFuture<String> x = new CompletableFuture<>();
            executor.execute(() -> {
                try {
                    Thread.sleep(3000);
                    x.complete("[value]asyncLoad from another thread after 3 seconds");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return x;
        }
    };

    private static final AsyncLoadingCache<String, String> demoCacheCF = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .maximumSize(2000)
            .buildAsync(loaderCF);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        CompletableFuture<String> v1 = demoCacheCF.get("key1");
        v1.whenComplete((s, throwable) -> System.out.println("s=" + s));

        System.out.println("v1.hashCode()=" + v1.hashCode());

        CompletableFuture<String> v2 = demoCacheCF.get("key1");
        System.out.println("v2.hashCode()=" + v2.hashCode());
        System.out.println("v1 == v2 : " + (v1 == v2));
        System.out.println("v2="+v2.get());

        Thread.sleep(10000);
    }
}
