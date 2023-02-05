package com.jianhongl.fresh.servelt;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lijianhong Date: 2023/1/27 Time: 7:56 PM
 * @version $
 */
@WebServlet(urlPatterns = "/postFile", asyncSupported = false)
public class ServletInputStreamReadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream reqInputStream = req.getInputStream();
        byte[] buf = new byte[1024];
        int nRead = 0;
        while (!reqInputStream.isFinished() && (nRead = reqInputStream.read(buf)) != -1) {
            System.out.println("nRead = " + nRead);
            System.out.println("readBytes:" + Arrays.toString(buf));
        }
    }
}
