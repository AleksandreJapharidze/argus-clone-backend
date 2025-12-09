package com.example.argusclone.repositories;

import com.example.argusclone.entities.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Override
    @EntityGraph(attributePaths = {"instructors"})
    Optional<Course> findById(Integer id);

    @Override
    @EntityGraph(attributePaths = {"instructors"})
    List<Course> findAll();

    @EntityGraph(attributePaths = {"instructors"})
    Optional<Course> findByCourseName(String courseName);

    @EntityGraph(attributePaths = {"instructors"})
    Optional<Course> findByCourseCode(String courseCode);
}
