/**
 * @project demo-thread-status V1.0
 * @Title:  SuperEntity.java   
 * @Package org.example.demo-thread-status.aid 
 * Copyright(c) 2020 BianPeng Co. Ltd. 
 * All right reserved. 
 */
package org.example.demo-thread-status.aid;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;

/**   
 * @Description:TODO(父级实体类)   
 * @author: BianPeng
 * @date:   2020年2月15日      
 */
public class SuperEntity implements Serializable{

	private static final long serialVersionUID = 4737580753737304591L;

	@ApiModelProperty(value = "ID")
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	@TableField(value = "uid_insert", fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建人")
	private Long uidInsert;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime utcInsert;

	@TableField(value = "uid_update", fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "修改人")
	private Long uidUpdate;

	@ApiModelProperty(value = "修改时间")
	private LocalDateTime utcUpdate;

	public Long getId() {
		return id;
	}
	
}
