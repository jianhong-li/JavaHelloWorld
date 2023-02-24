package com.jianhongl.fresh.servelt;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *     测试命令: curl -w "@./debug/curl-format.txt"  -s -v -o a.txt   http://localhost:8080/demo_async_servlet_war_exploded/curl/echoWithDelay?delay=5
 * </pre>
 * @author lijianhong Date: 2023/2/24 Time: 4:11 PM
 * @version $
 */
@WebServlet(urlPatterns = "/curl/echoWithDelay", asyncSupported = false)
public class CurlEchoDelayServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
