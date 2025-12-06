package com.example.argusclone.repositories;

import com.example.argusclone.entities.StudentCourseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCourseResultRepository extends JpaRepository<StudentCourseResult, Integer> {
    List<StudentCourseResult> findByStudentId(Integer studentId);
    boolean existsByStudentIdAndCourseId(Integer studentId, Integer courseId);
    void deleteByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
