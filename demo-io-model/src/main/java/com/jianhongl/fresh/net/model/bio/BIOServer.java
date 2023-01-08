package com.jianhongl.fresh.net.model.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <pre>
 *     Tomcat 7 的默认的IO模型是BIO. (Linux下的Tomcat 是 NIO 模型.)
 *     BIO模型下. 一个请求需要消耗一个线程.如果线程数是200.(Bio-Thread-max=200) 那最大的并发就是200.
 *     同时, 这个时候能够建立的连接数好像是1024.
 *     而在NIO模式下. Tomcat可以建立的连接数修改为了10000个.
 * </pre>
 * @author lijianhong Data: 2021/1/27 Time: 5:38 PM
 * @version $Id$
 */
public class BIOServer {

    public static void main(String[] args) throws IOException, InterruptedException {

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


        System.out.println("ready to sleep");
        Thread.sleep(10000);

        System.out.println("close ....");
        socket.close();


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

    public static void testServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket();

        serverSocket.bind(new InetSocketAddress(9000));

        //serverSocket.
    }
}

