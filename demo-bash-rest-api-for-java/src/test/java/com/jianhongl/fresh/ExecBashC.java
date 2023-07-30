package com.jianhongl.fresh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.junit.Test;

/**
 * @author lijianhong Date: 2023/7/30 Time: 4:24 PM
 * @version $
 */
public class ExecBashC {


    public static void main(String[] args) {
        try {
            // 这个命令是可以执行的
//            String[] cmd = {"bash", "-c", "ls;pwd"};
//            Process process = Runtime.getRuntime().exec(cmd);

            // 也可以这样写
//            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", "ls;pwd"});

            // 这样是不对的
            Process process = Runtime.getRuntime().exec("bash -c \"ls;pwd\"");

            // 读取子进程的输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 读取子进程的错误输出
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            // 等待子进程执行完毕
            int exitCode = process.waitFor();
            System.out.println("子进程执行完毕，退出代码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mainV2() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", "pwd;echo $0", ";pwd", "ls"});

            // 读取子进程的输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 读取子进程的错误输出
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            // 等待子进程执行完毕
            int exitCode = process.waitFor();
            System.out.println("子进程执行完毕，退出代码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void mainV3() {
        try {
            Process process = Runtime.getRuntime().exec("bash -c 'ls && pwd'");

            // 读取子进程的输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 读取子进程的错误输出
            InputStream errorStream = process.getErrorStream();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
            }

            // 等待子进程执行完毕
            int exitCode = process.waitFor();
            System.out.println("子进程执行完毕，退出代码：" + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用单引号来包裹bash -c 这个命令. 无法执行多个命令.
     */
    @Test
    public void mainV4() {
        try {
            String command = "bash -c 'ls && pwd'";
            Process process = Runtime.getRuntime().exec(command);

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Read the errors of the process
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对于bash -c 这个直接放到一个字符串里面. 使用了双引号. 也是无法执行多个命令的.
     */
    @Test
    public void mainV5() {
        try {
            String command = "bash -c \"ls && pwd\"";
            Process process = Runtime.getRuntime().exec(command);

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Read the errors of the process
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 最终地使用exec可以执行多个命令的版本.
     * https://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
     */
    @Test
    public void mainV6() {
        try {
            String[] command = {"bash", "-c", "ls && pwd"};
            Process process = Runtime.getRuntime().exec(command);

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Read the errors of the process
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
