/**
 * @project fuse-sso V1.0
 * @Title:  WebConfigurer.java   
 * @Package com.fuse.sso.config 
 * Copyright(c) 2020 BianPeng Co. Ltd. 
 * All right reserved. 
 */
package org.example.demo-thread-status.plugins.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:TODO(登录拦截)
 * @author: BianPeng
 * @date: 2020年2月20日
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
		registry.addInterceptor(LoginInterceptor()).addPathPatterns("/**");
	}

	@Bean
	public LoginInterceptor LoginInterceptor() {
		return new LoginInterceptor();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
				.allowCredentials(true).maxAge(3600).allowedHeaders("*");
	}
}
