/**
 * @project fuse-sso V1.0
 * @Title:  MetaHandler.java   
 * @Package com.fuse.sso.intercept 
 * Copyright(c) 2020 BianPeng Co. Ltd. 
 * All right reserved. 
 */
package org.example.demo-thread-status.plugins.mybatis;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.example.demo-thread-status.aid.Const;

/**   
 * @Description:TODO(处理新增和更新的基础数据填充，配合BaseEntity和MyBatisPlusConfig使用)   
 * @author: BianPeng
 * @date:   2020年2月19日      
 */
@Component
public class SqlMetaHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.setFieldValByName("uidInsert", this.getUid(), metaObject);
		this.setFieldValByName("uidUpdate", this.getUid(), metaObject);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName("uidUpdate", this.getUid(), metaObject);
	}
	
	/**   
	 * @Title: getuid   
	 * @Description: TODO(描述这个方法的作用)   
	 * @param:       
	 * @return: void      
	 * @throws   
	 */
	private long getUid() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			Long sessionUid = (Long) request.getSession().getAttribute(Const.SESSION_USER_ID);
			return sessionUid;
		} catch (Exception e) {
			// TODO: handle exception
			return 0L;
		}
	}

}
