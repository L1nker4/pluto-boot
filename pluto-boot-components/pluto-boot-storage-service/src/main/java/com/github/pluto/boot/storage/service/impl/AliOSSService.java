package com.github.pluto.boot.storage.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.github.pluto.boot.storage.exception.UploadException;
import com.github.pluto.boot.storage.property.AliOSSProperties;
import com.github.pluto.boot.storage.service.AbstractStorageService;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * @author Yalong Wei
 * @date 2024-05-28 下午4:14
 */
@Slf4j
public class AliOSSService extends AbstractStorageService {


    private final AliOSSProperties aliOSSProperties;

    private final OSS ossClient;

    public AliOSSService(AliOSSProperties aliOSSProperties, OSS ossClient) {
        this.aliOSSProperties = aliOSSProperties;
        this.ossClient = ossClient;
    }


    @Override
    public String uploadFile(byte[] data, String fileName) {
        try {
            String objectName = this.randomFileName(fileName);
            ossClient.putObject(
                    aliOSSProperties.getBucketName(),
                    objectName,
                    new ByteArrayInputStream(data));
            return objectName;

        } catch (OSSException e) {
            log.error("OSSException occurred: {}", e.getMessage());
            throw new UploadException();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
