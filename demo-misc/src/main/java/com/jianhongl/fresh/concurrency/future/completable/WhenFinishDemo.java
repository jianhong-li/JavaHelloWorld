package com.jianhongl.fresh.concurrency.future.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public class WhenFinishDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> x = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return true;
        }).whenComplete(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) {
                System.out.println("原始v=" + aBoolean);
            }
        });

        System.out.println("x=" + x.get());

    }
}
