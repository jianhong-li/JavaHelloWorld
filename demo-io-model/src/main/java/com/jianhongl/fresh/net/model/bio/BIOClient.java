package com.jianhongl.fresh.net.model.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author lijianhong Data: 2021/1/27 Time: 5:40 PM
 * @version $Id$
 */
public class BIOClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建客户端套接字 & 连接服务器
        Socket socket = new Socket("127.0.0.1", 9000);

        System.out.println("the connect to server:" + socket);

        //拿到输入流 -- server write to client, client read from server
        InputStream in = socket.getInputStream();
        //拿到输出流 -- client write to server
        OutputStream out = socket.getOutputStream();

        System.out.println("BIO连接成功,待待10秒后进行通信....");
        Thread.sleep(10000);

        byte[] send = "hello".getBytes();
        while (true){
            //client write to server
            out.write(send);
            byte[] buf = new byte[32];
            //read from server
            int len = in.read(buf, 0 ,send.length);
            //如果len == 1，说明server已经断开连接
            if(len == -1){
                throw  new RuntimeException("连接已断开");
            }
            System.out.println("recv:" + new String(buf, 0, len));
            Thread.sleep(1000);
        }
    }

}
