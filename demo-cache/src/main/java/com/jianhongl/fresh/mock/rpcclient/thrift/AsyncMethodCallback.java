package com.jianhongl.fresh.mock.rpcclient.thrift;

//  copy from:  org.apache.thrift.async.AsyncMethodCallback
// thrift 异步调用接口.
public interface AsyncMethodCallback<T> {
    /**
     * This method will be called when the remote side has completed invoking
     * your method call and the result is fully read. For oneway method calls,
     * this method will be called as soon as we have completed writing out the
     * request.
     * @param response
     */
    public void onComplete(T response);

    /**
     * This method will be called when there is an unexpected clientside
     * exception. This does not include application-defined exceptions that
     * appear in the IDL, but rather things like IOExceptions.
     * @param exception
     */
    public void onError(Exception exception);
}
