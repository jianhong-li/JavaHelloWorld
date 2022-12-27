package org.example.demo-thread-status.plugins.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * <p>
 * 说明： spring session失效，启用redis-session
 * </P>
 * 
 * @version: V1.0
 * @author: BianPeng
 * @time 2020年2月13日
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class HttpSessionConfig {
}
