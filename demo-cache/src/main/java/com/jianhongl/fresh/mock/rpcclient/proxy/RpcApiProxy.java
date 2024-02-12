package com.jianhongl.fresh.mock.rpcclient.proxy;

import com.jianhongl.fresh.mock.rpcclient.client.AsyncCallFunc;
import com.jianhongl.fresh.mock.rpcclient.api.RpcAPI;
import com.jianhongl.fresh.mock.rpcclient.client.AsyncRpcClient;
import com.jianhongl.fresh.mock.rpcclient.thrift.AsyncMethodCallback;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RpcApiProxy {

    private AsyncRpcClient<RpcAPI> asyncRpcClient;


    public RpcApiProxy(AsyncRpcClient<RpcAPI> asyncRpcClient) {
        this.asyncRpcClient = asyncRpcClient;
    }

    public CompletableFuture<List<Integer>> getVipPurchasedPackageAsyncFullName(long ytkUserId) {
        return asyncRpcClient.asyncCall(new AsyncCallFunc<RpcAPI, List<Integer>>() {
            @Override
            public void call(RpcAPI impl, AsyncMethodCallback<List<Integer>> cbk) throws Exception {
                impl.getVipPurchasedPackageAsync(ytkUserId, cbk);
            }
        });
    }

    public CompletableFuture<Set<Integer>> getVipPurchasedPackageAsync(long ytkUserId) {
        return asyncRpcClient.asyncCall((impl, cbk) -> impl.getVipPurchasedPackageAsync(ytkUserId, cbk));
    }
}
