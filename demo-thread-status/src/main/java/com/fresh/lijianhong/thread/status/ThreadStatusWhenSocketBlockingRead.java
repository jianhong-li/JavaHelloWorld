package com.fresh.lijianhong.thread.status;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import org.apache.commons.io.IOUtils;

/**
 * @author lijianhong Date: 2023/1/8 Time: 11:06 PM
 * @version $
 */
public class ThreadStatusWhenSocketBlockingRead {

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        Thread subInputThread = new Thread(() -> {
            try {
                serverThread();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "ServerSocket:"); // 线程的名字

        // 启动
        subInputThread.start();

        int count =0;
        while ((++count) < 100) {
            // 确保run已经得到执行
            Thread.sleep(1000);

            // 状态为RUNNABLE
            System.out.println("线程状态:" + subInputThread.getState());
        }
    }

    public static void serverThread() throws IOException, InterruptedException {
        //创建服务端套接字 & 绑定host:port & 监听client
        ServerSocket serverSocket = new ServerSocket(9000);

        // 这里设置一下超时时间.
        // java.net.SocketTimeoutException: Accept timed out
        // 如果设置了如上的等待时间后.接收客户端的连接就会有5秒以后超时. 因为是服务端.这个可以设置为0这样可以一直不超时.

        //serverSocket.setSoTimeout(5000);

        //等待客户端连接到来 ( 这个方法会阻塞,走到一个客户端连接到来 )
        Socket socket = serverSocket.accept();

        //拿到输入流 -- client write to server
        InputStream in = socket.getInputStream();

        //拿到输出流 -- server write to client
        OutputStream out = socket.getOutputStream();

        System.out.println("socket created[" + socket + "]: ready to read");
//        Thread.sleep(10000);
//
//        System.out.println("close ....");
//        socket.close();


        while (true) {
            //将数据读到buf中
            byte[] buf = new byte[32];
            //server read from client
            int len = in.read(buf);
            //如果len == 1，说明client已经断开连接
            if (len == -1) {
                throw new RuntimeException("连接已断开");
            }

            System.out.println("recv:" + new String(buf, 0, len));

            //将读出来的数据写回给client
            //如果不使用偏移量，可能会将buf中的无效数据也写回给client
            out.write(buf, 0, len);
        }
    }

}
