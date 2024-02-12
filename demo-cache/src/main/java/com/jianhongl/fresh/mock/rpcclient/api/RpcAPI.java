package com.jianhongl.fresh.mock.rpcclient.api;

import com.jianhongl.fresh.mock.rpcclient.thrift.AsyncMethodCallback;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface RpcAPI {

    /**
     * 第二个参数为非泛型版本. 这就为后面的潜在问题埋下了隐患.
     * @param ytkUserId 用户id
     * @param callback 回调函数
     * @return 可完成的future:  CompletableFuture<List<Integer>>
     */
    CompletableFuture<List<Integer>> getVipPurchasedPackageAsync(long ytkUserId, AsyncMethodCallback callback);

    // 如果是非泛型版本,这个错误可以被避免.
    // CompletableFuture<List<Integer>> getVipPurchasedPackageAsync(long ytkUserId, AsyncMethodCallback<List<Integer>> callback);
}
