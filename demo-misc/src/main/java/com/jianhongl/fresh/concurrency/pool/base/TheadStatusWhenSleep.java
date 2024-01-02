package com.jianhongl.fresh.concurrency.pool.base;

public class TheadStatusWhenSleep {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("start to sleep....");
                Thread.sleep(10000);
                System.out.println("sleep end....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        // 等待0.5秒, 确保线程已经进入sleep状态
        // 检验处于sleep状态的线程的状态
        try {
            System.out.println("sleep 0.5 seconds");
            Thread.sleep(500);
            System.out.println("sleep end....");
            System.out.println("thread status: " + thread.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
