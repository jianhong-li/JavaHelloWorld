package com.jianhongl.fresh.mock.rpcclient.async;

/**
 * 异步回调接口,一般意义上的异步回调接口
 *
 * @param <T> 返回值类型
 */
public interface AsyncCallback<T> {

    void onSuccess(T result);

    void onFailed(Exception error);
}
