package com.example.argusclone.repositories;

import com.example.argusclone.entities.StudentCourseResult;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentCourseResultRepository extends JpaRepository<StudentCourseResult, Integer> {
    @EntityGraph(attributePaths = {"student", "course"})
    List<StudentCourseResult> findByStudentId(Integer studentId);
    Optional<StudentCourseResult> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
    void deleteByCourseId(Integer courseId);
}
