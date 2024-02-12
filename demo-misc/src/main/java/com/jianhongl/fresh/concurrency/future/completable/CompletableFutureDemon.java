package com.jianhongl.fresh.concurrency.future.completable;

import static java.util.concurrent.CompletableFuture.runAsync;

import com.jianhongl.fresh.concurrency.future.completable.model.CFModelA;
import com.jianhongl.fresh.concurrency.future.completable.model.CFModelB;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/3/20 Time: 7:28 PM
 * @version $
 */
public class CompletableFutureDemon {

    private static Logger logger = LoggerFactory.getLogger(CompletableFutureDemon.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * {@link CompletableFuture#supplyAsync(Supplier)} 会提供一个完成的值. 并且是通过运行在一个异步线程池. 把运行结果作为完成值.
         */
        CompletableFuture<CFModelB> fb = CompletableFuture.supplyAsync(() -> {
            System.out.println("start to construct CFModelA....");
            CFModelA modelA = new CFModelA();
            modelA.setId(1);
            modelA.setName("Jim");

            System.out.println("sleep 1s....");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return modelA;
        }).thenCompose(cfModelA -> {
            System.out.println("输入名称:" + cfModelA.getName());
            CFModelB modelB = new CFModelB(100, cfModelA.getName() + "::Composed");
            System.out.println("complete b");

            if (modelB.getId() == 100) {
                return CompletableFuture.completedFuture(modelB);
            }
            throw new RuntimeException("模拟错误");

        });

        System.out.println("执行结果: "+ fb.get());
        System.out.println("THREAD:"+fb.get());

    }

}
