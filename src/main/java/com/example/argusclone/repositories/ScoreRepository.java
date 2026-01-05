package com.example.argusclone.repositories;

import com.example.argusclone.entities.Score;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    @EntityGraph(attributePaths = {"student", "course"})
    List<Score> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
