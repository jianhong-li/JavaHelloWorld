/**
 * @project fuse-sso V1.0
 * @Title:  LoginRequired.java   
 * @Package com.fuse.sso.interfaces 
 * Copyright(c) 2020 BianPeng Co. Ltd. 
 * All right reserved. 
 */
package org.example.demo-thread-status.plugins.mvc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在需要登录验证的Controller的方法上使用此注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})// 可用在类或接口或方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface LoginRequired {
	
}