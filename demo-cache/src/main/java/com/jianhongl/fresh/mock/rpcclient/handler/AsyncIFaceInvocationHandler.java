package com.jianhongl.fresh.mock.rpcclient.handler;

import com.jianhongl.fresh.mock.rpcclient.thrift.AsyncMethodCallback;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executors;

/**
 * 代理模式下的异步接口调用: 实际处理类.
 */
public class AsyncIFaceInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AsyncMethodCallback<?> rpcCallback = (AsyncMethodCallback<?>) args[args.length - 1];
        Object[] requestArgs = Arrays.copyOfRange(args, 0, args.length - 1);

        this.doInvoke(method, requestArgs, rpcCallback);
        return null;
    }

    private void doInvoke(Method method, Object[] requestArgs, AsyncMethodCallback rpcCallback) {

        System.out.println("通用JDK代理API处理开始.....");
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                System.out.println("rpc call start....");
                System.out.println("sleep 5s");
                Thread.sleep(5000);
                System.out.println("end......");
                ArrayList<Integer> data = new ArrayList<>();
                data.add(1);
                data.add(2);
                data.add(3);
                rpcCallback.onComplete(data);

            } catch (Exception e) {
                rpcCallback.onError(e);
            }
        });
    }

}
