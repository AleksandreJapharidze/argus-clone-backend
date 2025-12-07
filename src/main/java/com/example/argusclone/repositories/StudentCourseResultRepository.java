package com.example.argusclone.repositories;

import com.example.argusclone.entities.StudentCourseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentCourseResultRepository extends JpaRepository<StudentCourseResult, Integer> {
    List<StudentCourseResult> findByStudentId(Integer studentId);
    Optional<StudentCourseResult> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
