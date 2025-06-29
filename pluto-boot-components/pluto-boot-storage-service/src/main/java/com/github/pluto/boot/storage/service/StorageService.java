package com.github.pluto.boot.storage.service;

import com.github.pluto.boot.storage.exception.UploadException;

/**
 * @author Yalong Wei
 * @date 2024-05-28 下午3:57
 */
public interface StorageService {
    /**
     * upload file
     * @param data file data
     * @param fullPath file name
     * @return file url
     */
    String uploadFile(byte[] data, String fullPath) throws UploadException;

    byte[] downloadFile(String fullPath) throws UploadException;
}
