package com.jianhongl.fresh.net.model.biowithmultithread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Data: 2021/1/27 Time: 5:44 PM
 * @version $Id$
 */
public class BIOServerWithMultiThread {
    private static Logger logger = LoggerFactory.getLogger(BIOServerWithMultiThread.class);

    public static void main(String[] args) throws IOException {
        logger.info("start server.......");

        //创建服务端套接字 & 绑定host:port & 监听client
        ServerSocket serverSocket = new ServerSocket(9999);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        while (true) {
            // 等待客户端连接到来 ( 这个方法会阻塞,走到一个客户端连接到来 )
            final Socket socket = serverSocket.accept();

            logger.info("获取到连接:{}", socket);

            // 拿到输入流 -- client write to server
            final InputStream in = socket.getInputStream();

            // 拿到输出流 -- server write to client
            final OutputStream out = socket.getOutputStream();


            executorService.submit(() -> {

                while (true) {
                    try {
                        //将数据读到buf中
                        byte[] buf = new byte[32];
                        //server read from client
                        int len = 0;
                        len = in.read(buf);

                        //如果len == -1，说明client已经断开连接
                        if (len == -1) {
                            throw new RuntimeException("连接已断开");
                        }

                        System.out.println("recv:" + new String(buf, 0, len));

                        //将读出来的数据写回给client
                        //如果不使用偏移量，可能会将buf中的无效数据也写回给client
                        out.write(buf, 0, len);
                    } catch (Exception e) {
                        logger.error("error", e);
                        break;
                    }

                }
                logger.info("end.....................................................");
            });
        }
    }
}
