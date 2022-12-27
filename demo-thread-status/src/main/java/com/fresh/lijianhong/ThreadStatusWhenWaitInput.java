package com.fresh.lijianhong;

import java.util.Scanner;
import org.apache.commons.io.IOUtils;


/**
 * <pre>
 *     让主线程新启一个子线程. 让子线程处于等待输入的状态.此时去读取子线程的状态.
 * </pre>
 * @author lijianhong Date: 2022/12/27 Time: 3:29 PM
 * @version $
 */
public class ThreadStatusWhenWaitInput {

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        Thread subInputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("[" + Thread.currentThread().getName() + "]: " + "请输入");
                    // 命令行中的阻塞读
                    String input = in.nextLine();
                    System.out.println("你的输入:" + input);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
        }, "输入输出"); // 线程的名字

        // 启动
        subInputThread.start();

        // 确保run已经得到执行
        Thread.sleep(100);

        // 状态为RUNNABLE
        System.out.println("线程状态:" + subInputThread.getState());
    }

}
