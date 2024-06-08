package com.github.pluto.boot.storage.property;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yalong Wei
 * @date 2024-05-28 下午5:33
 */
@Configuration
@ConfigurationProperties(prefix = "pluto.storage.minio")
@ConditionalOnProperty(prefix = "pluto.storage", name = "type", havingValue = "minio")
@Data
public class MinioProperties {

        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
}
