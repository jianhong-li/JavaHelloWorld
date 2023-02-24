package com.jianhongl.fresh.servelt;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *     用于测试一个curl的时延相关的参数:
 *     测试命令: curl -w "@./debug/curl-format.txt"  -s -v -o a.txt -X POST -T "./doc/img/fake_ic_mem.jpeg"  http://localhost:8080/demo_async_servlet_war_exploded/postFile/curlTest?delay=5
 * </pre>
 * @author lijianhong Date: 2023/1/27 Time: 7:56 PM
 * @version $
 */
@WebServlet(urlPatterns = "/postFile/curlTest", asyncSupported = false)
public class ServletInputStreamReadServletWithDelay extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream reqInputStream = req.getInputStream();
        byte[] buf = new byte[1024*64];
        int nRead = 0;
        while (!reqInputStream.isFinished() && (nRead = reqInputStream.read(buf)) != -1) {
            System.out.println("nRead = " + nRead);
            //System.out.println("readBytes:" + Arrays.toString(buf));
        }

        String delay = req.getParameter("delay");
        if (delay != null && !delay.isEmpty()) {
            try {
                System.out.println("begin to sleep " + delay + "s");
                TimeUnit.SECONDS.sleep(Long.parseLong(delay));
                System.out.println("finish");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        resp.getWriter().write("OK");
    }
}
