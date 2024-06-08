package com.github.pluto.boot.storage.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pluto.storage.ali-oss")
@ConditionalOnProperty(prefix = "pluto.storage", name = "type", havingValue = "ali-oss")
@Data
public class AliOSSProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
