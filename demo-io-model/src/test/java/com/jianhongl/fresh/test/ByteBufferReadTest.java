package com.jianhongl.fresh.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/1/27 Time: 3:17 PM
 * @version $
 */
public class ByteBufferReadTest {

    private static Logger logger = LoggerFactory.getLogger(ByteBufferReadTest.class);

    //@Test
    public void testBuffer() throws IOException, URISyntaxException {
        System.out.println("===========================test start=====================================");
        byte[] source = new byte[256];
        byte[] target = new byte[256];

        for (int index = 0; index < source.length; index++) {
            source[index] = (byte) index;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(source);

        //       mark    pos     writeLimit
        // super(-1,     0,      lim,         cap,       new byte[cap],    0);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.clear();
        // 这个设置会导致认为buffer中已经有一个字节的内容. 因此在后面的写入位置是从1开始.而limit是5.因此容量(即可写入的内容)剩余为4
        buffer.position(1).limit(5);

        URL resource1 = getClass().getClassLoader().getResource("a.txt");
        System.out.println("resource1=======>:" + resource1);

        URL resource = Thread.currentThread().getContextClassLoader().getResource("a.txt");
        System.out.println("============>  a.txt path is:" + resource.getPath());

        FileChannel fileChannel = FileChannel.open(
            Paths.get(resource1.toURI()),
            new StandardOpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.READ}
        );

        int nRead = fileChannel.read(buffer);
        System.out.println("nRead = " + nRead);
        logger.info("before flip: buffer={}", buffer);

        buffer.flip();
        logger.info("after  flip: buffer={}", buffer);
        buffer.get(target,0,buffer.limit());

        System.out.println(Arrays.toString(target));

        Assert.assertEquals(0, target[0]);  // 因为buffer的位置0保留了,没有写入
        Assert.assertEquals(49, target[1]); // 第一个position位置接收文件的内容第一个字节
        Assert.assertEquals(0, target[5]);  // 因为超了limit,因此没有读取第5个字节

    }

}
