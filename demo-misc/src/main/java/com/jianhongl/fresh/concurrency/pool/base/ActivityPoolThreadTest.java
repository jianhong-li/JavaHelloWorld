package com.jianhongl.fresh.concurrency.pool.base;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用于测试一个线程在执行完成后, 经过了idle time后, 是否会被回收
 */
public class ActivityPoolThreadTest {


    public static void main(String[] args) {

        // 创建一个线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 4, 10,
                java.util.concurrent.TimeUnit.SECONDS,
                new java.util.concurrent.LinkedBlockingQueue<>(6),
                Executors.defaultThreadFactory()
        );

        // 提交10个任务,每隔1秒后任务模拟执行完成.
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    System.out.println(getTime() + Thread.currentThread().getName() + " - start ");
                    // sleep 1 秒
                    Thread.sleep(1000);
                    System.out.println(getTime() + Thread.currentThread().getName() + " -   end ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 等待0.5秒, 确保线程已经进入sleep状态
        try {
            System.out.println("sleep 0.5 seconds");
            Thread.sleep(500);
            System.out.println("sleep end....(此时线程池中的每一个任务都在执行第一个任务,且是在sleep状态)\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 0.5秒后, 线程池中的线程数应该是4个. 队列中应该是6个任务
        // 日志输出: [2024-01-02 17:57:18.280] [main] main - active thread count: 4, queue size: 6
        // 分析: 由于前面的任务我们做了1秒的模拟提交.因此在快速提交时肯定没有执行完成. 因此线程池中的线程数应该是4个. 队列中应该是6个任务
        //
        // 至于为什么 - sleep状态的线程 被计为了active task .
        // 原因是:
        // 线程中计数活跃的线程是通过计数处理中的线程数来计算的. 而sleep状态的线程, 也是处于处理中的线程.
        System.out.println(getTime() + Thread.currentThread().getName() + " - active thread count: " + executor.getActiveCount() + ", queue size: " + executor.getQueue().size() + "\n");


        // 当前情况
        // 队列: [5][4][3][2][1][0] ===> 6个任务在等待
        //                           |  4个线程在执行任务
        //                           + 线程-01  <--+----- 核心线程
        //                           + 线程-02  <--+
        //                           + 线程-03  _______ 非核心线程
        //                           + 线程-04  _______ 非核心线程
        // 填满过程:
        // 1. 先看当前的线程池中的线程数是否小于核心线程数, 如果小于, 则创建线程执行任务 . 直到创建2个core线程
        // 2. 如果大于等于核心线程数, 则将任务放入队列中 , 直到放入6个任务. 此时队列满了. (由于前面的任务我们做了1秒的模拟提交.因此在快速提交时肯定没有执行完成.
        // 3. 如果队列满了, 则创建非核心线程执行任务    ,  直到创建2个非核心线程


        try {
            Thread.sleep(3100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 6.5 秒后. 满满的3轮执行结束. 所有的线程都将空闲下来.
        System.out.println("\n所有的线程都将空闲下来 (此时没有task处理中的线程. active = 0 )");
        System.out.println(getTime() + Thread.currentThread().getName() +" - active thread count: " + executor.getActiveCount() + ", queue size: " + executor.getQueue().size()+"\n");

        System.out.println("再提交一个任务");
        executor.submit(() -> {
            try {
                System.out.println(getTime() + Thread.currentThread().getName() + " - start: ");
                int i = 0;
                for (i = 0; i < 10000000; i++) {

                }
                System.out.println("i=" + i);
                Thread.sleep(1000);
                System.out.println(getTime() + Thread.currentThread().getName() + " -   end: ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 第一个打印可以任务还没有执行.因此得到的是活跃为 0
        System.out.println("active thread count: " + executor.getActiveCount() + ", queue size: " + executor.getQueue().size());
        // 下面的打印, 由于任务已经开始执行, 因此得到的是活跃为 1
        System.out.println("active thread count: " + executor.getActiveCount() + ", queue size: " + executor.getQueue().size());
        System.out.println("active thread count: " + executor.getActiveCount() + ", queue size: " + executor.getQueue().size());
        System.out.println("active thread count: " + executor.getActiveCount() + ", queue size: " + executor.getQueue().size());

        // 休眠10秒, 等待线程执行完毕
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("active thread count: " + executor.getActiveCount());

        executor.shutdown();


    }

    private static String getTime() {
        return "[" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + "] " + "[" + Thread.currentThread().getName() + "] ";
    }
}
