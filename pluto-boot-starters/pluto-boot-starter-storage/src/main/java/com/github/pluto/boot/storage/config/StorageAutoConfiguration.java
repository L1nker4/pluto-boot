package com.github.pluto.boot.storage.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.pluto.boot.storage.property.AliOSSProperties;
import com.github.pluto.boot.storage.property.MinioProperties;
import com.github.pluto.boot.storage.service.StorageService;
import com.github.pluto.boot.storage.service.impl.AliOSSService;
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
    public StorageService minioFileService(MinioProperties minioProperties) {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(
                        minioProperties.getAccessKey(),
                        minioProperties.getSecretKey())
                .build();
        return new MinioService(minioProperties, minioClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "pluto.storage", name = "type", havingValue = "ali-oss")
    public StorageService aliOssFileService(AliOSSProperties aliOSSProperties) {
        OSS aliClient= new OSSClientBuilder().build(
                aliOSSProperties.getEndpoint(),
                aliOSSProperties.getAccessKeyId(),
                aliOSSProperties.getAccessKeySecret());
        return new AliOSSService(aliOSSProperties, aliClient);
    }

}
