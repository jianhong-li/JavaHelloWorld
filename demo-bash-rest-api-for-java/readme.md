# 文档说明

本示例工程演示了怎样使用 jq 以及 java的 ProcessBuilder 来对shell脚本的调用封装成一个标准的 rest_api格式. 比如:


```java
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args ) {
        logger.info("执行echo示例");
        System.out.println(BashRunner.runCommand("default", "echo", ImmutableMap.of("msg", "hello"), true, Maps.class));
    }
}
```

我们书写了一个echo的示例, 输入一个msg.返回的数据就是输入的msg:


```log
21:10:17.896 [main] INFO  com.jianhongl.fresh.App - 执行echo示例
21:10:17.908 [main] INFO  com.jianhongl.fresh.bash.support.BashRunner - 准备执行:script=default
21:10:18.137 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - 执行命令: launcher --script default --action echo --json {"msg":"hello"} -x
21:10:18.139 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - LAUNCHER_DIR:/Users/lijianhong/IdeaProjects/gitee/JavaHelloWorld/demo-bash-rest-api-for-java/script
21:10:18.176 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - ============CONF PARAM ==========
21:10:18.176 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - $script_name:default<=
21:10:18.177 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - =================================
21:10:18.208 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - ============JSON PARAM ==========
21:10:18.208 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - {
21:10:18.208 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner -   "msg": "hello"
21:10:18.208 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - }
21:10:18.208 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - =================================
21:10:18.210 [pool-2-thread-2] INFO  com.jianhongl.fresh.bash.support.BashRunner - call rest_api: [echo]
21:10:18.315 [main] INFO  com.jianhongl.fresh.bash.support.BashRunner - ===========>apiJson:
{
  "ret": true,
  "errcode": 1,
  "errmsg": "no error",
  "data": {
    "msg": "hello"
  }
}
<===============
APIResponse [ver='1.0', ret=true, errmsg='no error', errcode=1, data=com.google.common.collect.Maps@68267da0]
```