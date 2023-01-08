package com.fresh.lijianhong.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author lijianhong Date: 2022/12/27 Time: 4:36 PM
 * @version $
 */
public class ThreadStatusWaitAcceptTest {

    @Test
    public void testBlockedSocketState() throws Exception {
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(10086);
                    while (true) {
                        // 阻塞的accept方法
                        Socket socket = serverSocket.accept();
                        // TODO
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "socket线程"); // 线程的名字
        serverThread.start();

        // 确保run已经得到执行
        Thread.sleep(500);

        // 状态为RUNNABLE
        Assert.assertEquals(serverThread.getState(), Thread.State.RUNNABLE);

    }

}
