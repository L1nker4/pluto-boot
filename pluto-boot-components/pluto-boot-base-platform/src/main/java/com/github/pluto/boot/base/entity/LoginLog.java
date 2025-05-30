package com.github.pluto.boot.base.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;

import java.util.Date;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/18 12:13
 * @description： 登录日志实体类
 */
@Table("sys_login_log")
@Data
public class LoginLog {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户 ID
     */
    private String username;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 登录IP
     */
    private String ip;
}
