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
 * <pre>
 *     参考代码来源: <a href="https://www.javaboy.org/2021/0602/webflux-async-servlet.html">异步 Servlet 都不懂，谈何 WebFlux？</a>
 * </pre>
 * @author lijianhong Date: 2023/1/20 Time: 9:53 PM
 * @version $
 */
@WebServlet(urlPatterns = "/async",asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        AsyncContext asyncContext = request.startAsync();
        CompletableFuture.runAsync(() -> printLog(asyncContext, asyncContext.getRequest(), asyncContext.getResponse()));
        System.out.println("总耗时：" + (System.currentTimeMillis() - start));
    }

    private void printLog(AsyncContext asyncContext, ServletRequest request, ServletResponse response){
        try {
            Thread.sleep(3000);
            response.getWriter().write("ok");
            asyncContext.complete();
            System.out.println("[" + Thread.currentThread().getName() + "] "+"async complete");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}