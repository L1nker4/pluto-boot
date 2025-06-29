package com.github.pluto.boot.storage.config;


import com.github.pluto.boot.storage.property.MinioProperties;
import com.github.pluto.boot.storage.service.StorageService;
import com.github.pluto.boot.storage.service.impl.MinioService;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "pluto.storage", name = "enable", havingValue = "true")
public class StorageAutoConfiguration {


    @Bean
    @ConditionalOnProperty(prefix = "pluto.storage", name = "type", havingValue = "minio")
    public StorageService minioService(MinioProperties minioProperties) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(
                        minioProperties.getAccessKey(),
                        minioProperties.getSecretKey())
                .build();
        return new MinioService(minioProperties, minioClient);
    }

}
