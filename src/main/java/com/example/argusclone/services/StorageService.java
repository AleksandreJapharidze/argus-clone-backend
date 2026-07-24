package com.example.argusclone.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    byte[] downloadFile(Integer courseId, String fileName);
    void uploadFile(Integer courseId, MultipartFile file);
}
