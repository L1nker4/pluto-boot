package com.github.pluto.boot.rate.limiter.annotation;




import com.github.pluto.boot.rate.limiter.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/4 13:19
 * @description： 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRateLimit {

    // 资源名称，用于描述接口功能
    String name() default "";

    // 资源 key
    String key() default "";

    // key prefix
    String prefix() default "";

    // 时间的，单位秒
    int period();

    // 限制访问次数
    int count();

    // 限制类型
    LimitType limitType() default LimitType.CUSTOMER;
}
