package com.example.argusclone.services;

import com.example.argusclone.dtos.material.MaterialResponse;
import com.example.argusclone.entities.Course;
import com.example.argusclone.entities.Material;
import com.example.argusclone.entities.pending.OrphanedFile;
import com.example.argusclone.exceptions.ResourceNotFoundException;
import com.example.argusclone.mappers.MaterialMapper;
import com.example.argusclone.repositories.CourseRepository;
import com.example.argusclone.repositories.MaterialRepository;
import com.example.argusclone.repositories.OrphanedFileRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaterialService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialService.class);

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final StorageService storageService;
    private final CourseRepository courseRepository;
    private final OrphanedFileRepository orphanedFileRepository;

    public MaterialService(MaterialRepository materialRepository,
                           MaterialMapper materialMapper,
                           StorageService storageService,
                           CourseRepository courseRepository,
                           OrphanedFileRepository orphanedFileRepository) {
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
        this.storageService = storageService;
        this.courseRepository = courseRepository;
        this.orphanedFileRepository = orphanedFileRepository;
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
                                try {
                                    storageService.deleteFile(courseId, fileName);
                                    LOGGER.error("File deleted successfully after commit failure: {}", fileName);
                                } catch (Exception e) {
                                    OrphanedFile orphanedFile = setupOrphanedFile(courseId, fileName);
                                    orphanedFileRepository.save(orphanedFile);
                                    LOGGER.error("File orphaned after commit failure: {}", fileName);
                                }
                            }
                        }
                    }
            );
        }

        LOGGER.info("File uploaded successfully: {}", fileName);
        return materialMapper.toResponse(material);
    }

    @Transactional
    public void deleteMaterialById(Integer courseId, Integer materialId) {
        Material material = materialRepository.findById(materialId).orElseThrow(
                () -> new ResourceNotFoundException("Material with an id of " + materialId + " not found")
        );

        materialRepository.delete(material);

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        try {
                            storageService.deleteFile(courseId, material.getFileName());
                            LOGGER.info("File deleted successfully after commit: {}", material.getFileName());
                        } catch (Exception e) {
                            OrphanedFile orphanedFile = setupOrphanedFile(courseId, material.getFileName());
                            orphanedFileRepository.save(orphanedFile);
                            LOGGER.error("File orphaned after commit: {}", material.getFileName());
                        }
                    }
                }
        );

        LOGGER.info("Material deleted successfully: {}", materialId);
    }

    private OrphanedFile setupOrphanedFile(Integer courseId, String fileName) {
        OrphanedFile orphanedFile = new OrphanedFile();
        orphanedFile.setCourseId(courseId);
        orphanedFile.setFileName(fileName);
        return orphanedFile;
    }
}
