package com.jianhongl.fresh.servelt;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lijianhong Date: 2023/1/20 Time: 9:47 PM
 * @version $
 */
@WebServlet(urlPatterns = "/sync")
public class SyncServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        printLog(request, response);
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));
    }

    private void printLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response.getWriter().write("ok");
    }
}
