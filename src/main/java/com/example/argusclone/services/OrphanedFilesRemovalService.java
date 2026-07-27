package com.example.argusclone.services;

import com.example.argusclone.entities.pending.OrphanedFile;
import com.example.argusclone.repositories.OrphanedFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrphanedFilesRemovalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrphanedFilesRemovalService.class);

    private final OrphanedFileRepository orphanedFileRepository;
    private final StorageService storageService;

    public OrphanedFilesRemovalService(OrphanedFileRepository orphanedFileRepository,
                                       StorageService storageService) {
        this.orphanedFileRepository = orphanedFileRepository;
        this.storageService = storageService;
    }

    @Scheduled(cron = "0 0 3-7 * * *")
    private void removeOrphanedFiles() {
        List<OrphanedFile> orphanedFiles = orphanedFileRepository.find100MaxOrphanedFiles();
        orphanedFiles.forEach(orphanedFile -> {
            storageService.deleteFile(orphanedFile.getCourseId(), orphanedFile.getFileName());
            LOGGER.info("Orphaned file deleted from S3: {}", orphanedFile.getFileName());
            orphanedFileRepository.delete(orphanedFile);
            LOGGER.info("Orphaned file metadata deleted from DB: {}", orphanedFile.getFileName());
        });
    }
}
