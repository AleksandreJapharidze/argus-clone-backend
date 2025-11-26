package com.example.argusclone.repositories;

import com.example.argusclone.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
