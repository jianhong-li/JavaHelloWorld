package com.jianhongl.fresh;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import org.junit.Test;

/**
 * @author lijianhong Date: 2023/7/30 Time: 3:09 PM
 * @version $
 */
public class BashRunnerWithBuilder {


    /**
     * 演示使用 ProcessBuilder 执行 shell. 同时直接将子进程的输入输出连接到当前Java进程，方便查看输出
     */
    @Test
    public void testRunnerWithIOInherit() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c",
                                                               "echo currentPID:$$; ls -lash; pwd; echo 'hello world'; pwd;echo currentPID:$$");
            processBuilder.inheritIO(); // 将子进程的输入输出连接到当前Java进程，方便查看输出
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // 等待子进程执行完毕
            System.out.println("子进程执行完毕，退出代码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 演示使用 ProcessBuilder 执行 shell. 同时将子进程的ERROR输出重定向到当前Java进程
     */
    @Test
    public void testRunnerRedirectError() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ls", "-lash", "/pwd");
            processBuilder.redirectError(Redirect.INHERIT); // 将子进程的错误输出重定向到当前Java进程
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // 等待子进程执行完毕
            System.out.println("子进程执行完毕，退出代码：" + exitCode);
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
