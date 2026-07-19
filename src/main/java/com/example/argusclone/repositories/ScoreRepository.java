package com.example.argusclone.repositories;

import com.example.argusclone.entities.Score;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    @EntityGraph(attributePaths = {"student", "course"})
    List<Score> findByStudentIdAndCourseId(Integer studentId, Integer courseId);

    @EntityGraph(attributePaths = {"student", "course"})
    List<Score> findByCourseId(Integer courseId);

    @Modifying
    @Query("DELETE FROM Score s WHERE s.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Integer courseId);

    @Modifying
    @Query("DELETE FROM Score s WHERE s.student.id = :studentId")
    void deleteByStudentId(@Param("studentId") Integer studentId);

    boolean existsByCourseId(Integer courseId);
}
