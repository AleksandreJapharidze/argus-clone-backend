package com.example.argusclone.services;

import com.example.argusclone.exceptions.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class SupabaseStorageService implements StorageService {
    private final S3Client s3Client;
    private final CourseService courseService;

    public SupabaseStorageService(S3Client s3Client,
                                  CourseService courseService) {
        this.s3Client = s3Client;
        this.courseService = courseService;
    }

    @Override
    public byte[] downloadFile(Integer courseId, String filename) {
        courseService.getCourseById(courseId);

        String key = courseId + "/" + filename;
        return s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket("argus-clone-storage")
                        .key(key)
                        .build()
        ).asByteArray();
    }

    @Override
    public void uploadFile(Integer courseId, MultipartFile file) {
        courseService.getCourseById(courseId);

        String key = courseId + "/" + file.getOriginalFilename();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket("argus-clone-storage")
                            .contentType(file.getContentType())
                            .key(key)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new FileUploadException("Error uploading file " + file.getOriginalFilename() + " to S3\n" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
