package com.jianhongl.fresh.net.model.channel.file;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

/**
 * @author lijianhong Data: 2021/1/27 Time: 11:37 PM
 * @version $Id$
 */
public class FileChannelRead {
    public static void main(String[] args) throws IOException, InterruptedException {

        //创建文件通道
        FileChannel fileChannel = FileChannel.open(
                new File("a.txt").toPath(),
                new StandardOpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.READ}
                );

        //读缓冲区
        ByteBuffer readBuffer = ByteBuffer.allocate(32);
        int i = 0;
        while (true){
            //记录写入的字节数
            int wLen = fileChannel.write(ByteBuffer.wrap(("" + ++i) .getBytes()));
            //将文件的位置重置为写之前的位置，为下次读做准备
            //这里是需要特别注意的，如果没有这一步，你写是写成功了，但是读永远不会成功，因为此时position永远在末尾
            fileChannel.position(fileChannel.position() - wLen);
            //文件数据读到buffer中，读取完后，position的位置又回到上一步写结束的位置了
            int rLen = fileChannel.read(readBuffer);
            byte[] buf = new byte[rLen];
            //Buffer的读操作
            readBuffer.flip();
            readBuffer.get(buf);
            readBuffer.clear();
            System.out.println(new String(buf));
            Thread.sleep(1000);
        }
    }

}
