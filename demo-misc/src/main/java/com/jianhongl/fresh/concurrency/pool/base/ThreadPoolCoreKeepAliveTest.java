package com.jianhongl.fresh.concurrency.pool.base;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolCoreKeepAliveTest {
    public static void main(String[] args) {
        Executor executor = new ThreadPoolExecutor(
                1, 1, 4,
                java.util.concurrent.TimeUnit.SECONDS,
                new java.util.concurrent.LinkedBlockingQueue<>(6),
                Executors.defaultThreadFactory()
        );



        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("start to sleep....");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("sleep end....");
            }
        });

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
