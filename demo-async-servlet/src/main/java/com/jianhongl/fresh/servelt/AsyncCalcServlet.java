package com.jianhongl.fresh.servelt;

import static java.lang.System.out;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lijianhong Date: 2023/1/20 Time: 9:53 PM
 * @version $
 */
@WebServlet(urlPatterns = "/asyncCalc",asyncSupported = true)
public class AsyncCalcServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        AsyncContext asyncContext = request.startAsync();

        CompletableFuture<Integer> completableFuture =
            CompletableFuture.supplyAsync(
                () -> calcValue(asyncContext, asyncContext.getRequest(), asyncContext.getResponse())
            );

        completableFuture.whenComplete((integer, throwable) -> {
            try {
                PrintWriter out = response.getWriter();
                out.println("<!DOCTYPE html><html>");
                out.println("<head>");
                out.println("<meta charset=\"UTF-8\" />");
                String title = "helloworld.title";
                out.println("<title>" + title + "</title>");
                out.println("</head>");
                out.println("<body bgcolor=\"white\">");

                if (throwable == null) {
                    out.println("计算结果: "+integer);
                } else {
                    out.println("计算异常: "+integer);
                }

                out.println("</body>");
                out.println("</html>");

            } catch (Exception e) {
                // Fixme: response怎么优雅释放.
                try {
                    response.getWriter().write("计算异常: "+integer);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }finally {
                asyncContext.complete();
            }
        });


        out.println("总耗时：" + (System.currentTimeMillis() - start));
    }

    private int calcValue(AsyncContext asyncContext, ServletRequest request, ServletResponse response){
        try {
            Thread.sleep(3000);
            //response.getWriter().write("ok");
            out.println("[" + Thread.currentThread().getName() + "] "+"async complete");
            return 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}