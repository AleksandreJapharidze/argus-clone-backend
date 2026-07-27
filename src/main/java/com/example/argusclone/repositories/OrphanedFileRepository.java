package com.example.argusclone.repositories;

import com.example.argusclone.entities.pending.OrphanedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrphanedFileRepository extends JpaRepository<OrphanedFile, Integer> {
    @Query("SELECT o FROM OrphanedFile o ORDER BY o.id DESC LIMIT 100")
    List<OrphanedFile> find100MaxOrphanedFiles();
}
