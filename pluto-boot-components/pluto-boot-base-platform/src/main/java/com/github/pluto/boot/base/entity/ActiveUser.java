package com.github.pluto.boot.base.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pluto.boot.base.utils.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/19 13:46
 * @description： 在线用户
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActiveUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 2055229953429884344L;

    // 唯一编号
    private String id = RandomStringUtils.randomAlphanumeric(20);
    // 用户名
    private String username;
    // ip地址
    private String ip;
    // token(加密后)
    private String token;
    // 登录时间
    private String loginTime = DateUtil.formatFullTime(LocalDateTime.now(),DateUtil.FULL_TIME_SPLIT_PATTERN);
    // 登录地点
    private String loginAddress;
}
