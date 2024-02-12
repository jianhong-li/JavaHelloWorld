package com.jianhongl.fresh;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jianhongl.fresh.mock.rpcclient.api.RpcAPI;
import com.jianhongl.fresh.mock.rpcclient.client.AsyncRpcClientImpl;
import com.jianhongl.fresh.mock.rpcclient.enhance.CompletableFutureEx;
import com.jianhongl.fresh.mock.rpcclient.proxy.RpcApiProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Hello world!
 *
 */
public class App 
{

    /**
     * 方便测试, 全局的 future 对象. 用于观察 future 对象的状态
     */
    public static CompletableFuture globalFuture = null;

    public static RpcApiProxy rpcApiProxy = new RpcApiProxy(new AsyncRpcClientImpl<>(RpcAPI.class));

    public static AsyncCacheLoader<Long, Set<Integer>> loader = new AsyncCacheLoader<Long, Set<Integer>>() {
        @Override
        public @NonNull CompletableFuture<Set<Integer>> asyncLoad(@NonNull Long key, @NonNull Executor executor) {
            CompletableFuture<Set<Integer>> x = new CompletableFutureEx<>("caffeineDataFuture");
            globalFuture = rpcApiProxy.getVipPurchasedPackageAsync(key).whenComplete((integers, throwable) -> {
                if (throwable != null) {
                    System.out.println("error: " + throwable.getMessage());
                    x.complete(Collections.emptySet());
                } else {
                    System.out.println("cache loaded: " + integers);
                    x.complete(integers);
                }
            });
            return x;
        }
    };

    private static AsyncLoadingCache<Long, Set<Integer>> vipPurchasedPackageCacheCF = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .maximumSize(2000)
            .buildAsync(loader);

    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        // rpcApiProxy.getVipPurchasedPackageAsync(345L).get().getClass();
        CompletableFuture<Set<Integer>> future = vipPurchasedPackageCacheCF.get(1223L);
        try {
            System.out.println(future.get(8, TimeUnit.SECONDS));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("查看全局的 future 对象状态: " + globalFuture);
        }
    }
}
