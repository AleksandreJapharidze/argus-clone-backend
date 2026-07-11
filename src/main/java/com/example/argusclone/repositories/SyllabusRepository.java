package com.example.argusclone.repositories;

import com.example.argusclone.entities.Syllabus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, Integer> {
    @EntityGraph(attributePaths = {"course", "courseSchedule"})
    Optional<Syllabus> findByCourseId(Integer courseId);
    boolean existsByCourseId(Integer courseId);
}
