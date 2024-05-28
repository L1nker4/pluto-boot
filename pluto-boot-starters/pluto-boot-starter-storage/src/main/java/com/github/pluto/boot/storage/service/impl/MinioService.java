package com.github.pluto.boot.storage.service.impl;

import com.github.pluto.boot.storage.exception.UploadException;
import com.github.pluto.boot.storage.property.MinioProperties;
import com.github.pluto.boot.storage.service.AbstractStorageService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Yalong Wei
 * @date 2024-05-28 下午4:01
 */
@Slf4j
public class MinioService extends AbstractStorageService {

    private final MinioProperties minioProperties;

    private final MinioClient minioClient;

    public MinioService(MinioProperties minioProperties, MinioClient minioClient) {
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    @Override
    public String uploadFile(byte[] data, String fileName) {
        try {

            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            }
            String objectName = this.randomFileName(fileName);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(randomFileName(fileName))
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .build());
            return objectName;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UploadException();
        }
    }
}
