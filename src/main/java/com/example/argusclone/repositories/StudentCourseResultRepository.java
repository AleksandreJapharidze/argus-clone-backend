package com.example.argusclone.repositories;

import com.example.argusclone.entities.StudentCourseResult;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentCourseResultRepository extends JpaRepository<StudentCourseResult, Integer> {
    @EntityGraph(attributePaths = {"student"})
    List<StudentCourseResult> findByStudentId(Integer studentId);

    Optional<StudentCourseResult> findByStudentIdAndCourseName(Integer studentId, String courseName);

    @Modifying
    @Query("DELETE FROM StudentCourseResult s WHERE s.student.id = :studentId")
    void deleteByStudentId(Integer studentId);
}
