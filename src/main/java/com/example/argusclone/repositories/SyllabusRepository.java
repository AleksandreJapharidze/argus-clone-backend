package com.example.argusclone.repositories;

import com.example.argusclone.entities.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Integer> {
    Optional<Syllabus> findByCourseId(Integer courseId);
}
