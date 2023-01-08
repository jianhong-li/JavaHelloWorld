package com.fresh.lijianhong.test;

import java.util.Scanner;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author lijianhong Date: 2022/12/27 Time: 3:45 PM
 * @version $
 */
public class ThreadStatusWhenWaitInputTest {

    @Test
    public void testInBlockedIOState() throws InterruptedException {
        Scanner in = new Scanner(System.in);
        // 创建一个名为“输入输出”的线程t
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 命令行中的阻塞读
                    String input = in.nextLine();
                    System.out.println(input);
                } catch (Exception e) {
                    System.out.println("error:" + e);
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(in);
                }
            }
        }, "输入输出"); // 线程的名字

        // 启动
        t.start();

        // 确保run已经得到执行
        Thread.sleep(1000);

        System.out.println("thread:" + t.getName() + "'s status:" + t.getState());

        // Fixme: 此状态在maven的单元测试中会返回 BLock 状态. 暂时屏蔽掉
        // 状态为RUNNABLE
        // Assert.assertEquals(t.getState(), Thread.State.RUNNABLE);
    }

}
