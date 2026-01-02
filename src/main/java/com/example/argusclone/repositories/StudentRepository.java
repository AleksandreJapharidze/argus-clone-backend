package com.example.argusclone.repositories;

import com.example.argusclone.entities.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByName(String name);
    Optional<Student> findByEmail(String email);

    @Query("SELECT COUNT(g) FROM Student s JOIN s.groups g WHERE s.id = :studentId")
    int countStudentGroups(Integer studentId);

    @EntityGraph(attributePaths = {"groups"})
    @Query("SELECT s FROM Student s JOIN s.groups g WHERE g.id = :groupId AND g.course.id = :courseId")
    List<Student> findByGroupIdAndCourseId(Integer groupId, Integer courseId);
}
