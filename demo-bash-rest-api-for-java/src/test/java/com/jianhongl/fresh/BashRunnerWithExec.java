package com.jianhongl.fresh;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/7/30 Time: 1:54 PM
 * @version $
 */
public class BashRunnerWithExec {

    private static Logger logger = LoggerFactory.getLogger(BashRunnerWithExec.class);
    private static String workingDir = null;

    static {

        URL url = Thread.currentThread().getContextClassLoader().getResource("default.propeties");
        Preconditions.checkState(url != null);
        workingDir = url.getPath().substring(0, url.getPath().indexOf("demo-bash-rest-api-for-java"))
            + "demo-bash-rest-api-for-java/script";

    }

    @Test
    public void testSimpleExecShell() throws IOException {
        String cmd = "ls -lash";
        Process exec = Runtime.getRuntime().exec(cmd);
    }

    /**
     * 演示使用 Runtime.getRuntime().exec() 执行 shell
     */
    @Test
    public void testExecShellSuccess() throws IOException {
        String cmd = "ls -lash";
        Process exec = Runtime.getRuntime().exec(cmd);
        // 读取输出流: 读取命令执行后的输出结果
        InputStream inputStream = exec.getInputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        System.out.println("--------- msg from input stream ---------");
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        System.out.println("--------- msg from error stream ---------");
        InputStream errorStream = exec.getErrorStream();
        while ((len = errorStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
    }

    @Test
    public void testExecShellFail() throws IOException {
        String cmd = "ls -lash /pwd";
        Process exec = Runtime.getRuntime().exec(cmd);
        // 读取输出流: 读取命令执行后的输出结果
        InputStream inputStream = exec.getInputStream();

        byte[] bytes = new byte[1024];
        int len = 0;
        System.out.println("--------- msg from input stream ---------");
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
        System.out.println("--------- msg from error stream ---------");
        InputStream errorStream = exec.getErrorStream();
        while ((len = errorStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
    }

    @Test
    public void testMultiCommand() throws IOException {
        // String cmd = "bash -C 'for i in {1..100}; do echo $i; sleep 1;ls -lash; done'";
        String cmd = "bash -c 'ls;pwd'";
        //String cmd = "sh " + workingDir + "/echo_hello.sh";

        // 先开始执行命令
        Process exec = Runtime.getRuntime().exec(cmd);
        // 读取输出流: 读取命令执行后的输出结果
        InputStream inputStream = exec.getInputStream();

        System.out.println("start read");
        byte[] bytes = new byte[1024];
        int len = 0;
        System.out.println("--------- msg from input stream ---------");
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
    }


    @Test
    public void testBlockReadDelay() throws IOException, InterruptedException {

        String cmd = "sh " + workingDir + "/echo_hello.sh";

        // 先开始执行命令
        Process exec = Runtime.getRuntime().exec(cmd);
        // 读取输出流: 读取命令执行后的输出结果
        InputStream inputStream = exec.getInputStream();


        logger.info("sleep 20s...");
        // 不马上读取.我们先等待 10 秒钟
        Thread.sleep(20000);
        System.out.println("start read");
        byte[] bytes = new byte[1024];
        int len = 0;
        System.out.println("--------- msg from input stream ---------");
        while ((len = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, len));
        }
    }

}
