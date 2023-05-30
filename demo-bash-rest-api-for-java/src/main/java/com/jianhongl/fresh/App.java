package com.jianhongl.fresh;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.jianhongl.fresh.bash.support.BashRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args ) {
        logger.info("执行echo示例");
        System.out.println(BashRunner.runCommand("default", "echo", ImmutableMap.of("msg", "hello"), true, Maps.class));
    }
}
