package com.github.pluto.boot.base.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author     ：L1nker4
 * @date       ： 创建于  2020/1/22 15:18
 * @description： 
 */

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "base-platform")
public class BasePlatformProperties {

    private boolean openAopLog = true;

}
