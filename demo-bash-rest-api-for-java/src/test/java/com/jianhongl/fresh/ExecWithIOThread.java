package com.jianhongl.fresh;

import com.google.common.base.Preconditions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/7/30 Time: 5:15 PM
 * @version $
 */
public class ExecWithIOThread {

    private static Logger logger = LoggerFactory.getLogger(BashRunnerWithExec.class);
    private static String workingDir = null;

    static {

        URL url = Thread.currentThread().getContextClassLoader().getResource("default.propeties");
        Preconditions.checkState(url != null);
        workingDir = url.getPath().substring(0, url.getPath().indexOf("demo-bash-rest-api-for-java"))
            + "demo-bash-rest-api-for-java/script";

    }


    @Test
    public void testBlockReadDelay() throws IOException, InterruptedException {

        String cmd = "sh " + workingDir + "/echo_hello.sh";

        // 先开始执行命令
        Process exec = Runtime.getRuntime().exec(cmd);

        Thread t1 = new Thread(new PrintLogRunnable(exec.getInputStream()));
        Thread t2 = new Thread(new PrintLogRunnable(exec.getErrorStream()));
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    public static final class PrintLogRunnable implements Runnable {

        private InputStream in;

        public PrintLogRunnable(InputStream in) {
            this.in = in;
        }

        @Override
        public void run() {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    logger.info("{}", str);
                }

            } catch (Exception e) {
                logger.warn("{}", e);
            }
        }
    }
}
