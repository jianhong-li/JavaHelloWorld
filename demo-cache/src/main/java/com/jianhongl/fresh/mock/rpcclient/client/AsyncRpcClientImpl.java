package com.jianhongl.fresh.mock.rpcclient.client;

import com.jianhongl.fresh.mock.rpcclient.async.AsyncCallback;
import com.jianhongl.fresh.mock.rpcclient.enhance.CompletableFutureEx;
import com.jianhongl.fresh.mock.rpcclient.thrift.AsyncMethodCallback;
import com.jianhongl.fresh.mock.rpcclient.handler.AsyncIFaceInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * 代理异步RPC客户端的统一实现. 通过类型参数: AsyncIFace, 代表了具体的RPC客户端的接口类型
 *
 * @param <AsyncIFace> 具体类型参数
 */
public class AsyncRpcClientImpl<AsyncIFace> implements AsyncRpcClient<AsyncIFace> {

    /**
     * 实际的RPC调用实现, 我们传入一个JDK动态代理. 通过这个代理, 可以实现不同的RPC调用
     */
    private AsyncIFace asyncClient;

    private Class<AsyncIFace> clz;

    public AsyncRpcClientImpl(Class<AsyncIFace> clz) {
        this.clz = clz;
        asyncClient = (AsyncIFace) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new AsyncIFaceInvocationHandler());
    }

    @Override
    public <T> CompletableFuture<T> asyncCall(AsyncCallFunc<AsyncIFace, T> lambda) {
        CompletableFuture<T> future = new CompletableFutureEx<>("rpcDataFuture");

        // 由于 lambda 是属于一个 thrift 规范的接口.必须使用 thrift 的异步回调接口.
        // 但是 thrift 的回调是一个普通的完成接口. 为了将这个完成接口转换成 CompletableFuture, 我们需要在这里进行转换
        // 因此 定义了一个 asyncCall 函数. 用于将 thrift 的回调接口转换成 CompletableFuture.
        asyncCall(lambda, new AsyncCallback<T>() {

            @Override
            public void onSuccess(T result) {
                future.complete(result);
            }

            @Override
            public void onFailed(Exception error) {
                future.completeExceptionally(error);
            }
        });
        return future;
    }

    /**
     * 用于把 thrift 的异步回调接口转换成 普通的 AsyncCallback. 从而在普通的 AsyncCallback 完成转换: CompletableFuture
     * @param lambda 待调用函数接口
     * @param cbk 回调接口
     * @param <T>
     */
    private <T> void asyncCall(AsyncCallFunc<AsyncIFace, T> lambda, AsyncCallback<T> cbk) {
        AsyncMethodCallback<T> asyncMethodCallback = new AsyncMethodCallback<T>() {

            @Override
            public void onComplete(T s) {
                cbk.onSuccess(s);
            }

            @Override
            public void onError(Exception e) {
                cbk.onFailed(e);
            }
        };
        try {
            lambda.call(asyncClient, asyncMethodCallback);
        } catch (Exception e) {
            cbk.onFailed(e);
        }
    }

}
