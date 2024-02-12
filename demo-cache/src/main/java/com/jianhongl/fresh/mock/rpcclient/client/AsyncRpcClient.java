package com.jianhongl.fresh.mock.rpcclient.client;

import java.util.concurrent.CompletableFuture;

/**
 * 1) 代理异步RPC客户端的统一接口.
 * 2) 通过类型参数: AsyncIFace, 代表了具体的RPC客户端的接口类型
 *
 * @param <AsyncIFace> 异步RPC 接口类型参数
 */
public interface AsyncRpcClient<AsyncIFace> {
    <T> CompletableFuture<T> asyncCall(AsyncCallFunc<AsyncIFace, T> impl);
}
