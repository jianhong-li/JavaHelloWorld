package com.jianhongl.fresh.concurrency.lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/***
 * 用于测试线程在获取锁时, await() 会不会释放锁.
 */
public class LinkedBlockingQueueLockTest {

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue queue = new LinkedBlockingQueue();

        new Thread(() -> {
            try {
                Thread.currentThread().setName("thread1");
                System.out.println("thread1 start");
                queue.poll(10, java.util.concurrent.TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " get lock");
                Thread.sleep(1000);
                System.out.println("thread1 end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queue.offer(new Object());
            }
        }).start();


        Thread.sleep(5000);
        System.out.println("main thread");

        new Thread(() -> {
            try {
                Thread.currentThread().setName("thread2");
                System.out.println("thread2");
                queue.poll(10, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " get lock");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                queue.offer(new Object());
            }
        }).start();
    }
}
