package com.jianhongl.fresh.concurrency.pool.base;

import java.util.concurrent.*;

public class TheadStatusWhenSleepInSinglePool {

    private static Thread a = null;

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor executor = getThreadPoolExecutor();

        executor.submit(() -> {
            try {
                System.out.println("start to sleep....");
                a = Thread.currentThread();
                Thread.sleep(3000);
                System.out.println("sleep end....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        // 等待0.5秒, 确保线程已经进入sleep状态
        // 检验处于sleep状态的线程的状态
        try {
            System.out.println("sleep 0.5 seconds");
            Thread.sleep(500);
            System.out.println("sleep end....");
            System.out.println("thread active count: " + executor.getActiveCount());
            System.out.println("thread status: " + a.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread.sleep(3000);

        System.out.println("thread status isTerminated:" + executor.isTerminated());

        executor.shutdown();

    }

    private static ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        return executor;
    }
}
