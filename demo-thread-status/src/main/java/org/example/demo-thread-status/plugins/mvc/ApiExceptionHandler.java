package org.example.demo-thread-status.plugins.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.example.demo-thread-status.aid.JsonResult;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
	/**   
	 * @Title: handleException   
	 * @Description: TODO(处理异常)   
	 * @param: HttpServletRequest, Exception    
	 * @return: void      
	 * @throws   
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object handleException(HttpServletRequest request, Exception e) {
		log.error("===异常:"+e.getMessage());
		JsonResult<Object> result = new JsonResult<Object>();
		if (e instanceof RuntimeException) {
			result.error(e.getMessage());
		}else {
			result.error("sysout error!");
		}
		return result;
	}
	
}
