package com.github.pluto.boot.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/18 12:23
 * @description： 用户角色关系
 */
@Table("sys_user_role")
@Data
public class UserRole implements Serializable{
	
	@Serial
	private static final long serialVersionUID = -3166012934498268403L;

    @JsonSerialize(using = ToStringSerializer.class)
	private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
	private Long roleId;

}