package com.jianhongl.fresh.concurrency.future.completable;

import static java.util.concurrent.CompletableFuture.runAsync;

import com.jianhongl.fresh.concurrency.future.completable.model.CFModelA;
import com.jianhongl.fresh.concurrency.future.completable.model.CFModelB;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/3/20 Time: 7:28 PM
 * @version $
 */
public class CompletableFutureDemon {

    private static Logger logger = LoggerFactory.getLogger(CompletableFutureDemon.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<CFModelB> fb = CompletableFuture.supplyAsync(() -> {
            System.out.println("start to construct CFModelA....");
            CFModelA cfModelB = new CFModelA();
            cfModelB.setId(1);
            cfModelB.setName("Jim");

            logger.info("sleep 5s....");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return cfModelB;
        }).thenCompose(cfModelA -> {
            CFModelB modelB = new CFModelB(100, cfModelA.getName());
            logger.info("complete b");

            if (modelB.getId() == 100) {
                return CompletableFuture.completedFuture(modelB);
            }
            throw new RuntimeException("模拟错误");

        });

        logger.info("执行结果: {}", fb.get());
        System.out.println("THREAD"+fb.get());

    }

}
