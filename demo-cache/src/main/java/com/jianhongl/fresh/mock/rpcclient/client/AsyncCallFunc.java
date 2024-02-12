package com.jianhongl.fresh.mock.rpcclient.client;

import com.jianhongl.fresh.mock.rpcclient.thrift.AsyncMethodCallback;

/**
 * 异步调用RPC 函数 / 接口 / lamda
 * @param <AsyncIFace> 异步接口
 * @param <T> 返回值类型
 */
public interface AsyncCallFunc<AsyncIFace, T> {

    /**
     * 这个接口不管怎么实现. 最终类型参数只是一个类型约束.并没有真正地实现从这里面传入IFace. 最终还是要在实现中的实参中传入IFace
     * @param impl 传入的特定 thrift 接口实现. 例如 RpcAPI. 实际是一个JDK代理实现类. 这样可做到类是同一个. 但实际接口千千万.
     * @param cbk 回调函数.  这个是一个thrift规范接口.
     * @throws Exception
     */
    void call(AsyncIFace impl, AsyncMethodCallback<T> cbk) throws Exception;
}
