/**
 * @project fuse-sso V1.0
 * @Title:  AuthorityInterceptor.java   
 * @Package com.fuse.sso.intercept 
 * Copyright(c) 2020 BianPeng Co. Ltd. 
 * All right reserved. 
 */
package org.example.demo-thread-status.plugins.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import org.example.demo-thread-status.aid.Const;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:TODO(描述这个类的作用)
 * @author: BianPeng
 * @date: 2020年2月20日
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		log.info("request请求地址path[{}] uri[{}]", request.getServletPath(), request.getRequestURI());
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		// ①:START 方法注解级拦截器
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		LoginRequired classAnnotation = handlerMethod.getBean().getClass().getAnnotation(LoginRequired.class);
		if (null != classAnnotation) {
			return loginHandle(request);
		} else {
			// 判断接口是否需要登录
			LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
			// 有 @LoginRequired 注解，需要认证
			if (null != methodAnnotation) {
				return loginHandle(request);
			}
		}
		return true;
	}

	/**
	 * @Title: loginHandle @Description: TODO(登录验证处理)
	 * @param: request
	 * @return: Boolean 
	 * @throws
	 */
	private Boolean loginHandle(HttpServletRequest request) {
		Object session = request.getSession().getAttribute(Const.SESSION_USER);
		if (null != session) {
			return true;
		}
		return false;
	}
}
