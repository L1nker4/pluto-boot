package com.github.pluto.boot.storage.service;

import java.util.UUID;

/**
 * @author Yalong Wei
 * @date 2024-05-28 下午3:58
 */
public abstract class AbstractStorageService implements StorageService {

    @Override
    public abstract String uploadFile(byte[] data, String fileName);

    public String randomFileName(String fileName) {
        return UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
    };


}
