package com.example.argusclone.services;

import com.example.argusclone.dtos.material.MaterialResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Material;
import com.example.argusclone.exceptions.FileUploadException;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.MaterialMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final StorageService storageService;
    private final CourseRepository courseRepository;

    public MaterialService(MaterialRepository materialRepository,
                           MaterialMapper materialMapper,
                           StorageService storageService,
                           CourseRepository courseRepository) {
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
        this.storageService = storageService;
        this.courseRepository = courseRepository;
    }

    public List<MaterialResponse> getAllMaterialsByCourseId(Integer courseId) {
        return materialRepository.findByCourseId(courseId).stream()
                .map(materialMapper::toResponse)
                .toList();
    }

    @Transactional
    public MaterialResponse addNewMaterial(Integer courseId, MultipartFile file) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new ResourceNotFoundException("Course with an id of " + courseId + " not found")
        );

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        storageService.uploadFile(courseId, fileName, file);

        Material material = new Material();
        material.setFileName(fileName);
        material.setContentType(file.getContentType());
        material.setSize((int) file.getSize());
        material.setUploadedAt(LocalDateTime.now());
        material.setCourse(course);

        material = materialRepository.save(material);

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                                storageService.deleteFile(courseId, file.getOriginalFilename());
                                throw new FileUploadException("File upload failed. Please try again.");
                            }
                        }
                    }
            );
        }

        return materialMapper.toResponse(material);
    }

    @Transactional
    public void deleteMaterialById(Integer courseId, Integer materialId) {
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material with an id of " + materialId + " not found")
        );

        storageService.deleteFile(courseId, material.getFileName());
        materialRepository.delete(material);
    }
}
