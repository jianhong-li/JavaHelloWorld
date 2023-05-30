package com.jianhongl.fresh.bash.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.jianhongl.fresh.api.APIResponse;
import com.jianhongl.fresh.utils.JsonUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lijianhong Date: 2023/5/11 Time: 7:39 PM
 * @version $
 */
public class BashRunner {

    private static Logger logger = LoggerFactory.getLogger(BashRunner.class);

    private static String workingDir = null;

    static {

        URL url = Thread.currentThread().getContextClassLoader().getResource("default.propeties");
        Preconditions.checkState(url != null);
        workingDir = url.getPath().substring(0, url.getPath().indexOf("demo-bash-rest-api-for-java"))
            + "demo-bash-rest-api-for-java/script";

    }

    // Fixme: 随意配置的一个线程
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * ShellLauncher 执行器
     *
     * @param script    脚本名.默认脚本为 default , infra相关API 脚本名为: infra_api
     * @param action    动作 - 目前有:
     *                  default:
     *                  ____ echo
     * @param jsonParam 具体action操作的相关参数.
     * @param openDebug 是否打开调试日志
     * @param clz       返回RestAPI data类的Class
     * @param <T>       返回数据data的泛型
     * @return 操作结果
     */
    public static <T> APIResponse<T> runCommand(
        String script, String action,
        Map<String, Object> jsonParam,
        boolean openDebug,
        Class<T> clz) {

        logger.info("准备执行:script={}", script);
        try {
            // 参数对齐
            jsonParam = jsonParam == null ? new HashMap<>() : jsonParam;

            ProcessBuilder builder = new ProcessBuilder();

            // 设置脚本当前目录
            builder.directory(new File(workingDir));
            // 命令拼接
            builder.command("bash", "launcher",
                            "--script", script,
                            "--action", action,
                            "--json", JsonUtils.writeValue(jsonParam),
                            openDebug ? "-x" : " ");

            // 启动任务
            Process process = builder.start();

            APIResponseConsumer apiResponseConsumer = new APIResponseConsumer();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), apiResponseConsumer::add);

            Future<?> future = executorService.submit(streamGobbler);
            executorService.submit(new StreamGobbler(process.getErrorStream(), logger::info));

            int exitCode = process.waitFor();

            future.get(30, TimeUnit.MINUTES);

            // 正常返回的,返回包装的JSON,不正常返回的. 包装为规范的错误.
            if (exitCode == 0 || apiResponseConsumer.isJson()) {
                return apiResponseConsumer.get(clz);
            } else {
                return APIResponse.returnFail(exitCode, apiResponseConsumer.get());
            }
        } catch (Exception e) {
            logger.error("bashRunner_exe_failed:msg={}", e.getMessage(), e);
            return APIResponse.returnFail(-1, e.getMessage());
        }
    }

    /**
     * 流读取器
     */
    public static class StreamGobbler implements Runnable {

        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .forEach(consumer);
        }
    }

    /**
     * APIResponse 数据数据读取器
     */
    public static class APIResponseConsumer {

        private StringBuffer sb = new StringBuffer();
        private char[] tmp = new char[4];
        private boolean start = true;

        public void add(String line) {
            if (start) {
                sb.append(line);
            } else {
                sb.append("\n").append(line);
            }
            start = false;
        }

        public String get() {
            return sb.toString();
        }

        public boolean isJson() {
            if (sb.length() > 0) {
                sb.getChars(0, 1, tmp, 0);
                return tmp[0] == '{';
            }
            return false;
        }

        public <T> APIResponse<T> get(Class<T> clz) {

            String apiJson = get();
            logger.info("===========>apiJson:\n" + apiJson + "\n<===============");
            return JsonUtils.readValue(apiJson, new TypeReference<APIResponse<T>>() {
                @Override
                public Type getType() {
                    return TypeUtils.parameterize(APIResponse.class, clz);
                }
            });
        }
    }

}



