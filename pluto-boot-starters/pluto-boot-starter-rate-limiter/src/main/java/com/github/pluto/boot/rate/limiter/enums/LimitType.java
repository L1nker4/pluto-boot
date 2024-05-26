package com.github.pluto.boot.rate.limiter.enums;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2024/05/26 20:18
 * @description： 限流IP类型
 */
public enum LimitType {
    // 传统类型
    CUSTOMER,
    // 根据 IP 限制
    IP;
}
